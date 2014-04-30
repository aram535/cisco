package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:38
 */
@RunWith(MockitoJUnitRunner.class)
public class PreposConstructorTest {

    private static final Timestamp CURRENT_TIME = new Timestamp(DateTime.now().getMillis());
    private static final String CISCO_TYPE = "CISCO SB";
    private static final String PARTNER_NAME = "Partner Name";
    private static final String CLIENT_NUMBER = "158";
    private static final String CLIENT_NAME = "Client name";
    private static final String PART_NUMBER = "SPA112";
    private static final String AUTHORIZATION_NUMBER = "MDMF-4526117-1403";
    private static final String SERIALS = "ASDFEFE321321";
    private static final String COMMENT = "SOME COMMENT";
    private static final String BILL_NUMBER = "1267894";
    private static final String END_USER_NAME = "EndUserName";
    private static final String PROMO_CODE = "PP-SBFa81137-130126";

    private static final int ZIP = 61052;
    private static final double DISTI_DISCOUNT = 0.5;
    private static final double PROMO_DISCOUNT = 0.45;
    private static final int QUANTITY = 5;
    private static final double PRICE = 20.83;
    private static final int GPL = 25;
    private static final double SALE_DISCOUNT = 0.17;
    private static final double PRICELIST_DISCOUNT = 0.3;

    @InjectMocks
    private PreposConstructor preposConstructor = new DefaultPreposConstructor();

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

    private Sale firstSale;

    @Test
    public void thatGetPreposesReturnsEmptyListIfThereAreNoSales() {
        when(salesService.getSales(NOT_PROCESSED)).thenReturn(Lists.<Sale>newArrayList());
        List<PreposModel> preposes = preposConstructor.getNewPreposModels();
        assertThat(preposes).isNotNull().isEmpty();
    }

    @Test(expected = CiscoException.class)
    public void thatGetPreposesThrowsCiscoExceptionIfNoPriceFound() {

        mockRelatedServices();
        when(pricelistsService.getPricelistsMap()).thenReturn(Maps.<String, Pricelist>newHashMap());

        preposConstructor.getNewPreposModels();
    }

    @Test
    public void thatGetPreposesConstructsPreposWithClientNameFromSalesIfNoMatchingByClientNumber() {

        mockRelatedServices();

        when(clientsService.getClientsMap()).thenReturn(Maps.<String, Client>newHashMap());

        List<PreposModel> preposes = preposConstructor.getNewPreposModels();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);
        assertThat(preposes).isEqualTo(createExpectedPreposesForCaseWhenNoMatchingByClientNumber());
    }

    @Test
    public void thatGetPreposesCorrectlyMapsSecondPromo() {

        mockRelatedServices();

        List<PreposModel> preposes = preposConstructor.getNewPreposModels();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);


        Prepos actualPrepos = preposes.get(0).getPrepos();
        Prepos expectedPrepos = createExpectedPreposes().get(0).getPrepos();

        assertThat(actualPrepos.getSecondPromo()).isEqualTo(AUTHORIZATION_NUMBER);
        assertThat(actualPrepos).isEqualTo(expectedPrepos);
    }

    @Test
    public void thatWhenNoSuitableSecondPromoThenFirstPromoIsUsed() {

        mockRelatedServices();
        when(dartsService.getDartsTable()).thenReturn(createNonSuitableDarts());

        List<PreposModel> preposes = preposConstructor.getNewPreposModels();

        Prepos actualPrepos = preposes.get(0).getPrepos();
        assertThat(actualPrepos.getSecondPromo()).isNullOrEmpty();
        assertThat(actualPrepos.getFirstPromo()).isEqualTo(PROMO_CODE);
        assertThat(actualPrepos.getBuyDiscount()).isEqualTo(PROMO_DISCOUNT);
    }

    @Test
    public void thatWhenNoSuitableFirstAndSecondPromoThenPricelistDiscountIsUsed() {
        mockRelatedServices();
        when(dartsService.getDartsTable()).thenReturn(createNonSuitableDarts());
        when(promosService.getPromosMap()).thenReturn(Maps.<String, Promo>newHashMap());

        List<PreposModel> preposes = preposConstructor.getNewPreposModels();

        Prepos actualPrepos = preposes.get(0).getPrepos();
        assertThat(actualPrepos.getSecondPromo()).isNullOrEmpty();
        assertThat(actualPrepos.getFirstPromo()).isNullOrEmpty();
        assertThat(actualPrepos.getBuyDiscount()).isEqualTo(PRICELIST_DISCOUNT);
        assertThat(actualPrepos.getBuyPrice()).isEqualTo((1 - PRICELIST_DISCOUNT) * GPL);
    }

    @Test
    public void thatSuitableDartsBeingResolvedCorrectly() {

        mockRelatedServices();

        List<PreposModel> preposes = preposConstructor.getNewPreposModels();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);

        Map<String, Dart> suitableDarts = preposes.get(0).getSuitableDarts();
        assertThat(suitableDarts.size()).isEqualTo(2);
        assertThat(suitableDarts.size());
    }

    private void mockRelatedServices() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(createExpectedClients());
        when(pricelistsService.getPricelistsMap()).thenReturn(createExpectedPriceLists());
        when(dartsService.getDartsTable()).thenReturn(createExpectedDarts());
        when(promosService.getPromosMap()).thenReturn(createExpectedPromos());
    }

    private List<Sale> createExpectedSales() {

        firstSale = SaleBuilder.builder().id(1).shippedDate(CURRENT_TIME).shippedBillNumber(BILL_NUMBER).
                clientName(CLIENT_NAME).clientNumber(CLIENT_NUMBER).clientZip(ZIP).partNumber(PART_NUMBER).quantity(QUANTITY).
                serials(SERIALS).price(PRICE).ciscoType(CISCO_TYPE).comment(COMMENT).status(NOT_PROCESSED).build();

        return Lists.newArrayList(firstSale);
    }

    private Map<String, Pricelist> createExpectedPriceLists() {

        Map<String, Pricelist> pricelistsMap = Maps.newHashMap();

        Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setId(2L).setPartNumber(PART_NUMBER).
                setDescription("description").setDiscount(0.3).setGpl(GPL).setWpl(400).build();
        pricelistsMap.put(PART_NUMBER, pricelist);

        return pricelistsMap;
    }

    private Map<String, Client> createExpectedClients() {
        Map<String, Client> clients = Maps.newHashMap();
        Client client = new Client(1L, CLIENT_NUMBER, PARTNER_NAME, "Kiev", "Bazhana str. 36");
        clients.put(CLIENT_NUMBER, client);
        return clients;
    }

    private Map<String, Promo> createExpectedPromos() {
        Map<String, Promo> promos = Maps.newHashMap();
        Promo promo = new Promo(1L, PART_NUMBER, "2 Port Phone Adapter", PROMO_DISCOUNT, PROMO_CODE, GPL, PROMO_CODE, 5.52, 8);
        promos.put(PART_NUMBER, promo);
        return promos;
    }

    private Table<String, String, Dart> createExpectedDarts() {
        Table<String, String, Dart> darts = HashBasedTable.create();

        Dart dart1 = DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
                .setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1)
                .build();

        Dart dart2 = DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER + 2).setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER)
                .setDistiSku(PART_NUMBER).setListPrice(12).setClaimUnit(12).setExtCreditAmt(12)
                .setFastTrackPie(12).setIpNgnPartnerPricingEm(12).setMdmFulfillment(12).build();

        Dart dart3 = DartBuilder.builder().setId(1).setAuthorizationNumber("UNSUITABLE NUMBER").setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName("UNSUITABLE NAME").setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER)
                .setDistiSku(PART_NUMBER).setListPrice(12).setClaimUnit(12).setExtCreditAmt(12)
                .setFastTrackPie(12).setIpNgnPartnerPricingEm(12).setMdmFulfillment(12).build();

        darts.put(dart3.getCiscoSku(), dart3.getAuthorizationNumber(), dart3);
        darts.put(dart2.getCiscoSku(), dart2.getAuthorizationNumber(), dart2);
        darts.put(dart1.getCiscoSku(), dart1.getAuthorizationNumber(), dart1);

        return darts;
    }

    private Map<String, Dart> createSuitableDarts() {
        Map<String, Dart> darts = Maps.newHashMap();

        Dart dart1 = DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER + 2).setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(1)
                .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER)
                .setDistiSku("Disti").setListPrice(1).setClaimUnit(1).setExtCreditAmt(1)
                .setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1).build();

        Dart dart2 = DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER)
                .setDistiSku(PART_NUMBER).setListPrice(12).setClaimUnit(12).setExtCreditAmt(12)
                .setFastTrackPie(12).setIpNgnPartnerPricingEm(12).setMdmFulfillment(12).build();

        darts.put(dart1.getAuthorizationNumber(), dart1);
        darts.put(dart2.getAuthorizationNumber(), dart2);

        return darts;
    }

    private Table<String, String, Dart> createNonSuitableDarts() {
        Table<String, String, Dart> darts = HashBasedTable.create();

        Dart dart1 = DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
                .setDistributorInfo("ERC").setDistiDiscount(DISTI_DISCOUNT)
                .setResellerName("nonsuitable partner").setResellerCountry("Ukraine").setResellerAcct(123)
                .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1).setCiscoSku(PART_NUMBER)
                .setDistiSku("Disti").setListPrice(1).setClaimUnit(1).setExtCreditAmt(1)
                .setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1).build();

        darts.put(dart1.getCiscoSku(), dart1.getAuthorizationNumber(), dart1);

        return darts;
    }

    private List<PreposModel> createExpectedPreposes() {
        double buyPrice = (double) Math.round(GPL * (1 - DISTI_DISCOUNT) * 100) / 100;

        Prepos expectedPrepos = PreposBuilder.builder().type(CISCO_TYPE).partnerName(PARTNER_NAME).
                partNumber(PART_NUMBER).quantity(QUANTITY).salePrice(PRICE).saleDiscount(SALE_DISCOUNT).
                firstPromo(PROMO_CODE).secondPromo(AUTHORIZATION_NUMBER).buyDiscount(DISTI_DISCOUNT).
                buyPrice(buyPrice).clientNumber(CLIENT_NUMBER).endUser(END_USER_NAME).shippedDate(CURRENT_TIME).
                shippedBillNumber(BILL_NUMBER).comment(COMMENT).serials(SERIALS).zip(ZIP).ok(true).
                build();

        PreposModel preposModel = new PreposModel();
        preposModel.setPrepos(expectedPrepos);
        preposModel.setSuitableDarts(createSuitableDarts());

        return Lists.newArrayList(preposModel);
    }

    private List<PreposModel> createExpectedPreposesForCaseWhenNoMatchingByClientNumber() {
        double buyPrice = (double) Math.round(GPL * (1 - PROMO_DISCOUNT) * 100) / 100;

        Prepos expectedPrepos = PreposBuilder.builder().type(CISCO_TYPE).partnerName(CLIENT_NAME).
                partNumber(PART_NUMBER).quantity(QUANTITY).salePrice(PRICE).saleDiscount(SALE_DISCOUNT).
                firstPromo(PROMO_CODE).buyDiscount(PROMO_DISCOUNT).buyPrice(buyPrice).
                clientNumber(CLIENT_NUMBER).shippedDate(CURRENT_TIME).
                shippedBillNumber(BILL_NUMBER).comment(COMMENT).serials(SERIALS).zip(ZIP).ok(true).
                build();

        PreposModel preposModel = new PreposModel();
        preposModel.setPrepos(expectedPrepos);
        preposModel.setSuitableDarts(Maps.<String, Dart>newHashMap());

        return Lists.newArrayList(preposModel);
    }

}
