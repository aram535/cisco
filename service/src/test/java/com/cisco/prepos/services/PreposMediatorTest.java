package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:38
 */
@RunWith(MockitoJUnitRunner.class)
public class PreposMediatorTest {

    @InjectMocks
    private PreposMediator preposMediator = new DefaultPreposMediator();

    @Mock
    private SalesService salesService;

    @Mock
    private ClientsService clientsService;

    @Mock
    private PricelistsService pricelistsService;

    @Mock
    private PromosService promosService;

    @Mock
    private DartsService dartsService;

    @Mock
    private PreposModelConstructor preposModelConstructor;

    @Before
    public void mockRelatedServices() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(createExpectedClients());
        when(pricelistsService.getPricelistsMap()).thenReturn(createExpectedPriceLists());
        when(dartsService.getDartsTable()).thenReturn(createExpectedDarts());
        when(promosService.getPromosMap()).thenReturn(createExpectedPromos());
    }

    //TODO test mediator functionallity
    @Test
    public void emptyTest() {
        assertTrue(true);
    }


    private List<Sale> createExpectedSales() {

        return TestObjects.SalesFactory.newSaleList();
    }

    private Map<String, Pricelist> createExpectedPriceLists() {

        Map<String, Pricelist> pricelistsMap = Maps.newHashMap();

        Pricelist pricelist = TestObjects.PricelistsFactory.newPricelist();

        pricelistsMap.put(TestObjects.PART_NUMBER, pricelist);

        return pricelistsMap;
    }

    private Map<String, Client> createExpectedClients() {
        Map<String, Client> clients = Maps.newHashMap();

        Client client = TestObjects.ClientsFactory.newClient();

        clients.put(TestObjects.CLIENT_NUMBER, client);

        return clients;
    }

    private Map<String, Promo> createExpectedPromos() {
        Map<String, Promo> promos = Maps.newHashMap();

        Promo promo = TestObjects.PromosFactory.newPromo();

        promos.put(TestObjects.PART_NUMBER, promo);

        return promos;
    }

    private Table<String, String, Dart> createExpectedDarts() {
        Table<String, String, Dart> darts = HashBasedTable.create();

        Dart dart1 = TestObjects.DartsFactory.newDart(TestObjects.AUTHORIZATION_NUMBER, TestObjects.QUANTITY + 1);

        Dart dart2 = TestObjects.DartsFactory.newDart(TestObjects.AUTHORIZATION_NUMBER + 1, TestObjects.QUANTITY + 1);

        Dart dart3 = TestObjects.DartsFactory.newDart("UNSUITABLE NUMBER", TestObjects.QUANTITY + 1);

        darts.put(dart3.getCiscoSku(), dart3.getAuthorizationNumber(), dart3);
        darts.put(dart2.getCiscoSku(), dart2.getAuthorizationNumber(), dart2);
        darts.put(dart1.getCiscoSku(), dart1.getAuthorizationNumber(), dart1);

        return darts;
    }

    private Map<String, Dart> createSuitableDarts() {
        Map<String, Dart> darts = Maps.newHashMap();

        Dart dart1 = TestObjects.DartsFactory.newDart(TestObjects.AUTHORIZATION_NUMBER + 1, 1);

        Dart dart2 = TestObjects.DartsFactory.newDart(TestObjects.AUTHORIZATION_NUMBER, TestObjects.QUANTITY);

        darts.put(dart1.getAuthorizationNumber(), dart1);
        darts.put(dart2.getAuthorizationNumber(), dart2);

        return darts;
    }

    private Table<String, String, Dart> createNonSuitableDarts() {
        Table<String, String, Dart> darts = HashBasedTable.create();

        Dart dart1 = TestObjects.DartsFactory.newDart();
	    dart1.setResellerName("unsutable reseller name");

        darts.put(dart1.getCiscoSku(), dart1.getAuthorizationNumber(), dart1);

        return darts;
    }

    private List<PreposModel> createExpectedPreposes() {

        Prepos expectedPrepos = TestObjects.PreposFactory.newPrepos();
	    expectedPrepos.setBuyPrice(TestObjects.BUY_PRICE_WITH_DART);

        PreposModel preposModel = new PreposModel();
        preposModel.setPrepos(expectedPrepos);
        preposModel.setSuitableDarts(createSuitableDarts());

        return Lists.newArrayList(preposModel);
    }

}
