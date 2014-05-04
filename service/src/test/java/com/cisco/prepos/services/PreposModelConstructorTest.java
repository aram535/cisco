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
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartBuilder.builder;
import static com.cisco.prepos.dto.Prepos.Status.NOT_PROCESSED;
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

    private static final String PART_NUMBER = "part number";
    private static final String SECOND_PROMO = "second promo";

    private static final double DISCOUNT_FROM_PROVIDER = 0.35;
    private static final String CLIENT_NUMBER = "client number";
    private static final String CLIENT_NAME = "client name";
    private static final String PARTNER_NAME_FROM_PROVIDER = "partner name";
    private static final Timestamp SHIPPED_DATE = new Timestamp(DateTimeUtils.currentTimeMillis());
    private static final String SHIPPED_BILL_NUMBER = "shipped bill number";
    private static final String COMMENT = "comment";
    private static final String SERIALS = "serials";
    private static final int ZIP = 100500;
    private static final String CISCO_TYPE = "cisco type";
    private static final int QUANTITY = 5;
    private static final double SALE_PRICE = 100.5;
    private static final String FIRST_PROMO = "first promo";

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
        prepos = createPrepos();

        sale = new Sale();
        sale.setClientNumber(CLIENT_NUMBER);
        sale.setClientName(CLIENT_NAME);

        Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, FIRST_PROMO, SECOND_PROMO);
        when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(DISCOUNT_FROM_PROVIDER);
        when(partnerNameProvider.getPartnerName(sale, getClientsMap())).thenReturn(PARTNER_NAME_FROM_PROVIDER);
    }

    private Prepos createPrepos() {
        Prepos prepos = new Prepos();
        prepos.setPartnerName(PART_NUMBER);
        prepos.setPartnerName(PARTNER_NAME_FROM_PROVIDER);
        prepos.setStatus(NOT_PROCESSED);
        prepos.setClientNumber(CLIENT_NUMBER);
        prepos.setShippedDate(SHIPPED_DATE);
        prepos.setShippedBillNumber(SHIPPED_BILL_NUMBER);
        prepos.setComment(COMMENT);
        prepos.setSerials(SERIALS);
        prepos.setZip(ZIP);
        prepos.setType(CISCO_TYPE);
        prepos.setQuantity(QUANTITY);
        prepos.setSalePrice(SALE_PRICE);
        return prepos;
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
