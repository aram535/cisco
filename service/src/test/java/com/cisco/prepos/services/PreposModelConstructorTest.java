package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
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
import static com.cisco.testtools.TestObjects.*;
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

    @Mock
    private SuitableDartsProvider suitableDartsProvider;

    private Prepos prepos;

    private Sale sale;

    @Before
    public void init() {
        prepos = PreposFactory.newPrepos();

        sale = new Sale();
        sale.setClientNumber(CLIENT_NUMBER);
        sale.setClientName(CLIENT_NAME);
        sale.setQuantity(QUANTITY);
        sale.setPartNumber(PART_NUMBER);

        Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, FIRST_PROMO, SECOND_PROMO);
        when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(DISCOUNT_FROM_PROVIDER);
        when(partnerNameProvider.getPartnerName(sale, getClientsMap())).thenReturn(PARTNER_NAME_FROM_PROVIDER);
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME_FROM_PROVIDER, QUANTITY, getDartsTable())).thenReturn(Maps.<String, Dart>newHashMap());
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
        clientsMap.put(CLIENT_NUMBER, new Client());
        return clientsMap;
    }

    private Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = builder().build();
        table.put(PART_NUMBER, SECOND_PROMO, dart);
        return table;
    }

    private Map<String, Promo> getPromosMap() {
        Map<String, Promo> map = Maps.newHashMap();
        Promo promo = newPromoBuilder().build();
        map.put(PART_NUMBER, promo);
        return map;
    }

    private Map<String, Pricelist> getPriceMap() {
        Map<String, Pricelist> map = Maps.newHashMap();
        Pricelist pricelist = newPricelistBuilder().build();
        map.put(PART_NUMBER, pricelist);
        return map;
    }


}
