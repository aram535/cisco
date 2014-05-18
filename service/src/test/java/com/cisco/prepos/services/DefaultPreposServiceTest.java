package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartConstants;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartConstants.BLANK_AUTHORIZATION_NUMBER;
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
    private DartsService dartsService;

    @Mock
    private PreposConstructor preposConstructor;

    @Mock
    private PreposModelConstructor preposModelConstructor;

    @Mock
    private PreposUpdater preposUpdater;


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
    public void thatUpdateCallsPreposesDaoAndDartsServiceUpdateMethod() {

        List<PreposModel> preposModels = getAllPreposModels();
        List<Dart> darts = Lists.newArrayList();

        List<Prepos> preposes = newPreposes();
        when(preposModelConstructor.getPreposesFromPreposModels(preposModels)).thenReturn(preposes);
        when(dartsService.getDarts()).thenReturn(darts);

        preposService.update(preposModels);

        verify(preposesDao).update(preposes);
        verify(dartsService).update(darts);
    }

    private List<Prepos> newPreposes() {

        Prepos prepos = newPrepos();
        prepos.setPartnerName("new partner name");
        return Lists.newArrayList(prepos);
    }

    private List<PreposModel> getAllPreposModels() {

        Prepos prepos = newPrepos();
        Map<String, Dart> suitableDarts = ImmutableMap.of(BLANK_AUTHORIZATION_NUMBER, DartConstants.EMPTY_DART);
        PreposModel preposModel = new PreposModel(prepos, suitableDarts, DartConstants.EMPTY_DART);

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
