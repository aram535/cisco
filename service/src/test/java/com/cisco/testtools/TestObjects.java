package com.cisco.testtools;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.joda.time.DateTimeUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status.NOT_POS;

/**
 * Created by Alf on 05.05.2014.
 */
public class TestObjects {

    public static final String PART_NUMBER = "part number";

    public static final double BUY_DISCOUNT = 0.35;
    public static final String CLIENT_NUMBER = "client number";
    public static final String CLIENT_NAME = "client name";
    public static final String PARTNER_NAME = "partner name";
    public static final Timestamp SHIPPED_DATE = new Timestamp(DateTimeUtils.currentTimeMillis());
    public static final String SHIPPED_BILL_NUMBER = "shipped bill number";
    public static final String COMMENT = "comment";
    public static final String SERIALS = "serials";
    public static final int ZIP = 100500;
    public static final String CISCO_TYPE = "cisco type";
    public static final int QUANTITY = 5;
    public static final double SALE_PRICE = 200.5;

    public static final double DART_DISTI_DISCOUNT = 0.55;
    public static final double PROMO_DISCOUNT = 0.35;
    public static final double PRICE_LIST_DISCOUNT = 0.21;

    public static final Double GPL = 250d;

    public static final String CLIENT_CITY = "client city";
    public static final String CLIENT_ADDRESS = "client address";

    public static final String PROMO_CODE = "promo code";
    public static final String DISTRIBUTOR_INFO = "distributor info";
    public static final String AUTHORIZATION_NUMBER = "authorization number";

    public static final String END_USER_NAME = "end user name";

    public static final double SALE_DISCOUNT = 0.20;
    private static final double BUY_PRICE = 162.5;

    public static class PreposFactory {

        public static Prepos newSimplePrepos() {

            Prepos prepos = new Prepos();

            prepos.setPartNumber(PART_NUMBER);
            prepos.setPartnerName(PARTNER_NAME);
            prepos.setStatus(NOT_POS);
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

        public static Prepos newPrepos() {

            Prepos prepos = new Prepos();

            prepos.setPartNumber(PART_NUMBER);
            prepos.setPartnerName(PARTNER_NAME);
            prepos.setStatus(NOT_POS);
            prepos.setClientNumber(CLIENT_NUMBER);
            prepos.setShippedDate(SHIPPED_DATE);
            prepos.setShippedBillNumber(SHIPPED_BILL_NUMBER);
            prepos.setComment(COMMENT);
            prepos.setSerials(SERIALS);
            prepos.setZip(ZIP);
            prepos.setType(CISCO_TYPE);
            prepos.setQuantity(QUANTITY);
            prepos.setSalePrice(SALE_PRICE);
            prepos.setFirstPromo(PROMO_CODE);
            prepos.setSecondPromo(AUTHORIZATION_NUMBER);
            prepos.setEndUser(END_USER_NAME);
            prepos.setSaleDiscount(SALE_DISCOUNT);
            prepos.setBuyDiscount(BUY_DISCOUNT);
            prepos.setBuyPrice(BUY_PRICE);
            double posSum = (double) Math.round(BUY_PRICE * QUANTITY * 100) / 100;
            prepos.setPosSum(posSum);
            prepos.setOk(true);


            return prepos;
        }
        public static Prepos newPreposWithoutPromos() {

            Prepos prepos = new Prepos();

            prepos.setPartNumber(PART_NUMBER);
            prepos.setPartnerName(PARTNER_NAME);
            prepos.setStatus(NOT_POS);
            prepos.setClientNumber(CLIENT_NUMBER);
            prepos.setShippedDate(SHIPPED_DATE);
            prepos.setShippedBillNumber(SHIPPED_BILL_NUMBER);
            prepos.setComment(COMMENT);
            prepos.setSerials(SERIALS);
            prepos.setZip(ZIP);
            prepos.setType(CISCO_TYPE);
            prepos.setQuantity(QUANTITY);
            prepos.setSalePrice(SALE_PRICE);
            prepos.setFirstPromo("");
            prepos.setSecondPromo("");
            prepos.setEndUser(END_USER_NAME);
            prepos.setSaleDiscount(SALE_DISCOUNT);
            prepos.setBuyDiscount(BUY_DISCOUNT);
            prepos.setBuyPrice(BUY_PRICE);
            double posSum = (double) Math.round(BUY_PRICE * QUANTITY * 100) / 100;
            prepos.setPosSum(posSum);
            prepos.setOk(true);


            return prepos;
        }

        public static List<Prepos> newPreposList() {

            Prepos firstPrepos = newPrepos();

            return Lists.newArrayList(firstPrepos);
        }
    }

    public static class SalesFactory {

        public static Sale newSale() {

            Sale sale = SaleBuilder.builder().id(1).shippedDate(SHIPPED_DATE).shippedBillNumber(SHIPPED_BILL_NUMBER).
                    clientName(CLIENT_NAME).clientNumber(CLIENT_NUMBER).clientZip(ZIP).partNumber(PART_NUMBER).quantity(QUANTITY).
                    serials(SERIALS).price(SALE_PRICE).ciscoType(CISCO_TYPE).comment(COMMENT).status(Sale.Status.NEW).build();

            return sale;
        }

        public static List<Sale> newSaleList() {

            Sale firstSale = newSale();

            return Lists.newArrayList(firstSale);
        }

    }

    public static class PricelistsFactory {

        public static Pricelist newPricelist() {

            return PricelistBuilder.newPricelistBuilder().setId(2L).setPartNumber(PART_NUMBER).
                    setDescription("description").setDiscount(0.3).setGpl(GPL).setWpl(400).build();
        }

        public static Pricelist newPricelist(Double gpl) {

            return PricelistBuilder.newPricelistBuilder().setId(2L).setPartNumber(PART_NUMBER).
                    setDescription("description").setDiscount(0.3).setGpl(gpl).setWpl(400).build();
        }
    }

    public static class ClientsFactory {

        public static Client newClient() {

            return new Client(1L, CLIENT_NUMBER, PARTNER_NAME, CLIENT_CITY, CLIENT_ADDRESS);
        }

        public static List<Client> newClientList() {

            Client firstClient = new Client(1L, CLIENT_NUMBER, PARTNER_NAME, CLIENT_CITY, CLIENT_ADDRESS);

            return Lists.newArrayList(firstClient);
        }

        public static Map<String, Client> newClientMap() {

            return Maps.uniqueIndex(newClientList(), new Function<Client, String>() {
                @Override
                public String apply(Client client) {
                    return client.getClientNumber();
                }
            });
        }
    }

    public static class PromosFactory {

        public static Promo newPromo() {
            return new Promo(1L, PART_NUMBER, "2 Port Phone Adapter", PROMO_DISCOUNT, PROMO_CODE, GPL, PROMO_CODE, 5.52, 8, SHIPPED_DATE);
        }

        public static Promo newPromo(String code) {
            Promo promo = newPromo();
            promo.setCode(code);
            return promo;
        }
    }

    public static class DartsFactory {

        public static Dart newDart() {

            return DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
                    .setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
                    .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                    .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                    .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
                    .setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1)
                    .setMdmFulfillment(1)
                    .build();
        }

        public static Dart newDart(String authorizationNumber, String endUserName) {

            return DartBuilder.builder().setId(1).setAuthorizationNumber(authorizationNumber).setVersion(1)
                    .setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
                    .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                    .setEndUserName(endUserName).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                    .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
                    .setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1)
                    .setMdmFulfillment(1)
                    .build();
        }

        public static Dart newDart(String authorizationNumber, int quantity) {

            return DartBuilder.builder().setId(1).setAuthorizationNumber(authorizationNumber).setVersion(1)
                    .setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
                    .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                    .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(quantity)
                    .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
                    .setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1)
                    .build();
        }

        public static Dart newDart(Timestamp startDate, Timestamp endDate) {

            return DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
                    .setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
                    .setResellerName(PARTNER_NAME).setResellerCountry("Ukraine").setResellerAcct(123)
                    .setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
                    .setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
                    .setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1)
                    .setMdmFulfillment(1).setStartDate(startDate).setEndDate(endDate)
                    .build();
        }

        public static Table<String, String, Dart> getDartsTable() {
            Table<String, String, Dart> table = HashBasedTable.create();
            Dart dart = DartBuilder.builder().setDistiDiscount(DART_DISTI_DISCOUNT).build();
            table.put(PART_NUMBER, AUTHORIZATION_NUMBER, dart);
            return table;
        }
    }
}
