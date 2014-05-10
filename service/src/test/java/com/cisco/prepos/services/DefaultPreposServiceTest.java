package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NEW;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.SalesFactory.newSaleList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: Rost
 * Date: 07.05.2014
 * Time: 22:28
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposServiceTest {

    @InjectMocks
    private PreposService preposService = new DefaultPreposService();

    @Mock
    private PreposesDao preposesDao;

    @Mock
    private SalesService salesService;

    @Mock
    private PreposModelConstructor preposModelConstructor;

    @Mock
    private DartsService dartsService;

    @Mock
    private PreposUpdater preposUpdater;

    @Mock
    private PreposConstructor preposConstructor;

    @Mock
    private ClientsService clientsService;

    @Mock
    private PromosService promosService;

    @Mock
    private PricelistsService pricelistsService;

    @Before
    public void init() {
        when(clientsService.getClientsMap()).thenReturn(Maps.<String, Client>newHashMap());
        when(promosService.getPromosMap()).thenReturn(Maps.<String, Promo>newHashMap());
        when(pricelistsService.getPricelistsMap()).thenReturn(Maps.<String, Pricelist>newHashMap());
    }

    @Test
    public void thatIfAllPreposAreEmptyAndNoNewSalesReturnEmptyList() {
        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).getPreposes();
        verify(preposesDao).updateAll(anyList());
        verify(salesService).getSales(NEW);
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
        when(preposUpdater.updatePreposes(allPreposesWithEmptySerials)).thenReturn(allPreposes);
        when(dartsService.getDartsTable()).thenReturn(dartsTable);
	    when(preposModelConstructor.construct(allPreposes, Maps.<String, Pricelist>newHashMap(),
			    Maps.<String, Promo>newHashMap(), dartsTable)).thenReturn(allPreposModels);

        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).updateAll(allPreposes);
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
        when(preposUpdater.updatePreposes(allPreposesWithEmptySerials)).thenReturn(allPreposes);
        when(salesService.getSales(NEW)).thenReturn(newSales);
        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(preposConstructor.construct(newSales, Maps.<String, Client>newHashMap())).thenReturn(newPreposes);
        when(preposModelConstructor.construct(result, Maps.<String, Pricelist>newHashMap(), Maps.<String, Promo>newHashMap(), dartsTable)).thenReturn(allPreposModels);

        List<PreposModel> allData = preposService.getAllData();

        verify(preposesDao).updateAll(allPreposes);
        assertThat(allData).isEqualTo(allPreposModels);
    }

    private List<Prepos> newPreposes() {
        Prepos prepos = newPrepos();
        prepos.setPartnerName("new partner name");
        return Lists.newArrayList(prepos);
    }

    private List<PreposModel> getAllPreposModels() {
        Prepos prepos = newPrepos();
        Map<String, Dart> suitableDarts = ImmutableMap.of("", PreposModel.EMPTY_DART);
        PreposModel preposModel = new PreposModel(prepos, suitableDarts,PreposModel.EMPTY_DART);

        return Lists.newArrayList(preposModel);
    }

    public List<Prepos> getAllPreposes() {
        return Lists.newArrayList(newPrepos());
    }

    public List<Prepos> getAllPreposesWithEmptySerials() {
        Prepos prepos = newPrepos();
        prepos.setSerials("");

        return Lists.newArrayList(prepos);
    }

    public List<Sale> getAllSales() {
        return newSaleList();
    }
}
