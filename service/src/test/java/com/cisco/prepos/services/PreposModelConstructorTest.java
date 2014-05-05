package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartBuilder.builder;
import static com.cisco.pricelists.dto.PricelistBuilder.newPricelistBuilder;
import static com.cisco.promos.dto.PromoBuilder.newPromoBuilder;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 05.05.2014
 * Time: 0:31
 */
@RunWith(MockitoJUnitRunner.class)
public class PreposModelConstructorTest {

    @InjectMocks
    private PreposModelConstructor preposModelConstructor = new DefaultPreposModelConstructor();

    @Mock
    private DiscountProvider discountProvider;

    @Mock
    private PartnerNameProvider partnerNameProvider;

    private Prepos prepos;

    private Sale sale;

    @Before
    public void init() {
        prepos = TestObjects.PreposFactory.newPrepos();

        sale = new Sale();
        sale.setClientNumber(TestObjects.CLIENT_NUMBER);
        sale.setClientName(TestObjects.CLIENT_NAME);

        Triplet<String, String, String> discountInfo = new Triplet(TestObjects.PART_NUMBER, TestObjects.FIRST_PROMO, TestObjects.SECOND_PROMO);
        when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(TestObjects.DISCOUNT_FROM_PROVIDER);
        when(partnerNameProvider.getPartnerName(sale, getClientsMap())).thenReturn(TestObjects.PARTNER_NAME_FROM_PROVIDER);
    }

    @Test
    public void thatConstructNewModelsReturnsEmptyListIfSalesAreEmpty() {
        List<PreposModel> preposModels = preposModelConstructor.constructNewPreposModels(Lists.<Sale>newArrayList(), getClientsMap(), getPriceMap(), getPromosMap(), getDartsTable());
        assertThat(preposModels).isNotNull().isEmpty();
    }

    @Test(expected = CiscoException.class)
    public void thatConstructNewModelsThrowsCiscoExceptionIfPriceNotFoundForSale() {
        preposModelConstructor.constructNewPreposModels(getSales(), getClientsMap(), Maps.<String, Pricelist>newHashMap(), getPromosMap(), getDartsTable());
    }

    private List<Sale> getSales() {
        return Lists.newArrayList(sale);
    }

    private Map<String, Client> getClientsMap() {
        Map<String, Client> clientsMap = Maps.newHashMap();
        clientsMap.put(TestObjects.CLIENT_NUMBER, new Client());
        return clientsMap;
    }

    private Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = builder().build();
        table.put(TestObjects.PART_NUMBER, TestObjects.SECOND_PROMO, dart);
        return table;
    }

    private Map<String, Promo> getPromosMap() {
        Map<String, Promo> map = Maps.newHashMap();
        Promo promo = newPromoBuilder().build();
        map.put(TestObjects.PART_NUMBER, promo);
        return map;
    }

    private Map<String, Pricelist> getPriceMap() {
        Map<String, Pricelist> map = Maps.newHashMap();
        Pricelist pricelist = newPricelistBuilder().build();
        map.put(TestObjects.PART_NUMBER, pricelist);
        return map;
    }


}
