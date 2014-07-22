package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartAssistant;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.posready.service.PosreadyService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.DartApplier;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.fest.assertions.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartAssistant.BLANK_AUTHORIZATION_NUMBER;
import static com.cisco.sales.dto.Sale.Status.NEW;
import static com.cisco.testtools.TestObjects.AUTHORIZATION_NUMBER;
import static com.cisco.testtools.TestObjects.ClientsFactory.newClient;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.cisco.testtools.TestObjects.SalesFactory.newSaleList;
import static com.google.common.collect.ImmutableMap.of;
import static junitx.framework.ComparableAssert.assertEquals;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: Rost
 * Date: 07.05.2014
 * Time: 22:28
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposServiceTest {

    private static final String OTHER_END_USER = "otherEndUser";
    @InjectMocks
    private PreposService preposService = new DefaultPreposService();

    @Mock
    private PreposesDao preposesDao;

    @Mock
    private SalesService salesService;

    @Mock
    private DartsService dartsService;

    @Mock
    private PreposConstructor preposConstructor;

    @Mock
    private PreposModelConstructor preposModelConstructor;

    @Mock
    private PreposUpdater preposUpdater;

    @Mock
    private DartApplier dartApplier;

    @Mock
    private PricelistsService pricelistsService;

    @Mock
    private PromosService promosService;

    @Mock
    private PreposValidator preposValidator;

	@Mock
	private ClientsService clientsService;

	@Mock
	private PosreadyService posreadyService;

	private Map<String, Pricelist> pricelistMap = of(PART_NUMBER, newPricelist());
	private Table<String, String, Dart> dartsTable = getDartsTable();
	private Map<String, Promo> promosMap = of(PART_NUMBER, newPromo());
	private Map<String, Client> clientsMap = of(PART_NUMBER, newClient());

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void thatIfAllPreposAreEmptyAndNoNewSalesReturnEmptyList() {
        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).getPreposes();
        verify(preposesDao).update(anyList());
        verify(preposesDao).save(anyList());
        verify(salesService).getSales(NEW);
        verify(salesService).updateSalesStatuses(anyList());
        verifyNoMoreInteractions(preposesDao, salesService);

        assertThat(allData).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsPreposesAsFromPreposUpdaterIfNoNewSales() {

        List<Prepos> allPreposesWithEmptySerials = getAllPreposesWithEmptySerials();
        List<Prepos> allPreposes = getAllPreposes();
        Table<String, String, Dart> dartsTable = HashBasedTable.create();
        List<PreposModel> allPreposModels = getAllPreposModels();

        when(preposesDao.getPreposes()).thenReturn(allPreposesWithEmptySerials);
        when(preposUpdater.update(allPreposesWithEmptySerials)).thenReturn(allPreposes);
        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(preposModelConstructor.construct(allPreposes)).thenReturn(allPreposModels);

        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).update(allPreposes);
        assertThat(allData).isEqualTo(allPreposModels);
    }

    @Test
    public void thatReturnsPreposesAsFromPreposUpdaterWithConstructedFromNewSales() {

        List<Prepos> allPreposesWithEmptySerials = getAllPreposesWithEmptySerials();
        List<Prepos> allPreposes = getAllPreposes();
        Table<String, String, Dart> dartsTable = HashBasedTable.create();
        List<PreposModel> allPreposModels = getAllPreposModels();
        List<Sale> newSales = newSaleList();
        List<Prepos> newPreposes = newPreposes();

        List<Prepos> result = Lists.newArrayList(allPreposes);
        result.addAll(newPreposes);

        when(preposesDao.getPreposes()).thenReturn(allPreposesWithEmptySerials);
        when(preposUpdater.update(allPreposesWithEmptySerials)).thenReturn(allPreposes);
        when(salesService.getSales(NEW)).thenReturn(newSales);
        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(preposConstructor.construct(newSales)).thenReturn(newPreposes);
        when(preposModelConstructor.construct(result)).thenReturn(allPreposModels);

        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).update(allPreposes);
        assertThat(allData).isEqualTo(allPreposModels);
    }

    @Test
    public void thatUpdateCallsPreposesDaoIfValidatorProcceed() {

        List<PreposModel> preposModels = getAllPreposModels();
        List<Prepos> preposes = newPreposes();

        when(preposModelConstructor.getPreposes(preposModels)).thenReturn(preposes);

	    preposService.updateFromModels(preposModels);

        verify(preposesDao).update(preposes);
    }

    @Test
    public void thatRecountPreposReturnsRecountedPreposFromApplier() {
        Prepos newPrepos = newPrepos();
        Dart selectedDart = newDart();
        Map<String, Pricelist> pricelistMap = of(PART_NUMBER, newPricelist());
        Table<String, String, Dart> dartsTable = getDartsTable();
        Map<String, Promo> promosMap = of(PART_NUMBER, newPromo());

        when(pricelistsService.getPricelistsMap()).thenReturn(pricelistMap);
        when(promosService.getPromosMap()).thenReturn(promosMap);
        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(dartApplier.getPrepos(newPrepos, selectedDart, pricelistMap, dartsTable, promosMap)).thenReturn(getExpectedPrepos());

        Prepos prepos = preposService.recountPrepos(newPrepos, selectedDart);
        Assertions.assertThat(prepos).isEqualTo(getExpectedPrepos());
    }

	@Test
	public void thatValidationIsPerformedWhenvalidatePreposForSelectedDartIsCalled() {

		List<PreposModel> allPreposModels = getAllPreposModels();
		PreposModel firstPreposModel = allPreposModels.get(0);

		preposService.validatePreposForSelectedDart(allPreposModels, firstPreposModel);

		verify(preposValidator).validateDartQuantity(allPreposModels, firstPreposModel);
	}

	@Test
	public void thatFilteredPreposesBeingReturnedWhenStatusPassedToGetAllPreposes() {

		List<Prepos> allPreposesWithDifferentSatuses = getAllPreposesWithDifferentSatuses();
		List<Prepos> allNotProcessedPreposes = getAllPreposes();
		Table<String, String, Dart> dartsTable = HashBasedTable.create();
		List<PreposModel> allPreposModels = getAllPreposModels();

		when(preposesDao.getPreposes()).thenReturn(allPreposesWithDifferentSatuses);
		when(preposUpdater.update(allNotProcessedPreposes)).thenReturn(allNotProcessedPreposes);
		when(dartsService.getDartsTable()).thenReturn(dartsTable);
		when(preposModelConstructor.construct(allNotProcessedPreposes)).thenReturn(allPreposModels);

		List<PreposModel> allData = preposService.getAllData(Prepos.Status.NOT_POS);

		verify(preposesDao).update(allNotProcessedPreposes);
		assertThat(allData).isEqualTo(allPreposModels);
	}

	@Test
	public void thatPreposAndDartsAreUpdatedAfterPosreadyExport() {

		List<PreposModel> allPreposModels = getAllPreposModels();

		List<Prepos> allPreposes = getAllPreposes();

		when(preposModelConstructor.getPreposes(allPreposModels)).thenReturn(allPreposes);
		when(pricelistsService.getPricelistsMap()).thenReturn(pricelistMap);
		when(promosService.getPromosMap()).thenReturn(promosMap);
		when(dartsService.getDartsTable()).thenReturn(dartsTable);
		when(clientsService.getClientsMap()).thenReturn(clientsMap);

		Dart dartToUpdate = dartsTable.get(PART_NUMBER, AUTHORIZATION_NUMBER);
		when(dartApplier.updateDartQuantity(allPreposes, dartsTable)).thenReturn(Lists.newArrayList(dartToUpdate));

		String posreadyId = "123456";
		String expectedFilename = String.format("C:\\test\\%s.xls", posreadyId);
		when(posreadyService.exportPosready(allPreposes, clientsMap, pricelistMap, dartsTable, promosMap)).thenReturn(expectedFilename);

		String filename = preposService.exportPosready(allPreposModels);

		assertEquals(expectedFilename, filename);

		for (Prepos prepos : allPreposes) {
			assertEquals(Prepos.Status.WAIT, prepos.getStatus());
			assertEquals(posreadyId, prepos.getPosreadyId());
		}



		verify(dartsService, times(1)).update(Lists.newArrayList(dartToUpdate));
		verify(preposesDao, times(1)).update(updatedAfterPosreadyPreposes());

	}

	@Test
	public void thatExceptionIsThrownWhenPreposesWithIncorrectStatusPassedToExportPosready() {

		List<PreposModel> preposes = getAllPreposModels();
		Iterables.getOnlyElement(preposes).getPrepos().setStatus(Prepos.Status.WAIT);

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage("All preposes should be in " + Prepos.Status.NOT_POS.toString() + " status");

		preposService.exportPosready(preposes);

	}

	@Test
	public void thatExceptionIsThrownWhenTryingToExportPosreadyFromEmptyList() throws Exception {

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage("Nothing to export. Preposes list is empty");

		preposService.exportPosready(Lists.<PreposModel>newArrayList());
	}

	private List<Prepos> updatedAfterPosreadyPreposes() {
		Prepos prepos = newPrepos();
		prepos.setStatus(Prepos.Status.WAIT);
		prepos.setPosreadyId("123456");
		return Lists.newArrayList(prepos);
	}

    private Prepos getExpectedPrepos() {
        Prepos expected = newPrepos();
        expected.setEndUser(OTHER_END_USER);
        expected.setOk(false);
        return expected;
    }

    private List<Prepos> newPreposes() {

        Prepos prepos = newPrepos();
        prepos.setPartnerName("new partner name");
        return Lists.newArrayList(prepos);
    }

    private List<PreposModel> getAllPreposModels() {

        Prepos prepos = newPrepos();
        Map<String, Dart> suitableDarts = of(BLANK_AUTHORIZATION_NUMBER, DartAssistant.EMPTY_DART);
        PreposModel preposModel = new PreposModel(prepos, suitableDarts, DartAssistant.EMPTY_DART);

        return Lists.newArrayList(preposModel);
    }

	private List<Prepos> getAllPreposes() {
        return Lists.newArrayList(newPrepos());
    }

	private List<Prepos> getAllPreposesWithDifferentSatuses() {
		List<Prepos> allPreposes = getAllPreposes();
		Prepos prepos = newPrepos();
		prepos.setStatus(Prepos.Status.POS_OK);
		allPreposes.add(prepos);

		return allPreposes;
	}

	private List<Prepos> getAllPreposesWithEmptySerials() {
        Prepos prepos = newPrepos();
        prepos.setSerials("");

        return Lists.newArrayList(prepos);
    }

	private List<Sale> getAllSales() {
        return newSaleList();
    }
}
