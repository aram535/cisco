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
import com.google.common.collect.Lists;
import org.joda.time.DateTimeUtils;

import java.sql.Timestamp;
import java.util.List;

import static com.cisco.prepos.dto.Prepos.Status.NOT_PROCESSED;

/**
 * Created by Alf on 05.05.2014.
 */
public class TestObjects {

	public static final String PART_NUMBER = "part number";
	public static final String FIRST_PROMO = "first promo";
	public static final String SECOND_PROMO = "second promo";

	public static final double DISCOUNT_FROM_PROVIDER = 0.35;
	public static final String CLIENT_NUMBER = "client number";
	public static final String CLIENT_NAME = "client name";
	public static final String PARTNER_NAME_FROM_PROVIDER = "partner name";
	public static final Timestamp SHIPPED_DATE = new Timestamp(DateTimeUtils.currentTimeMillis());
	public static final String SHIPPED_BILL_NUMBER = "shipped bill number";
	public static final String COMMENT = "comment";
	public static final String SERIALS = "serials";
	public static final int ZIP = 100500;
	public static final String CISCO_TYPE = "cisco type";
	public static final int QUANTITY = 5;
	public static final double SALE_PRICE = 100.5;

	public static final double DART_DISTI_DISCOUNT = 0.35;
	public static final double PROMO_DISCOUNT = 0.55;
	public static final double PRICE_LIST_DISCOUNT = 0.21;

	public static final int GPL = 25;

	public static final String CLIENT_CITY = "client city";
	public static final String CLIENT_ADDRESS = "client address";

	public static final String PROMO_CODE = "promo code";
	public static final String DISTRIBUTOR_INFO = "distributor info";
	public static final String AUTHORIZATION_NUMBER = "authorization number";

	public static final String END_USER_NAME = "end user name";

	public static final double BUY_PRICE_WITH_DART = (double) Math.round(GPL * (1 - DART_DISTI_DISCOUNT) * 100) / 100;

	public static class PreposFactory {

		public static Prepos newPrepos() {

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
	}

	public static class SalesFactory {

		public static Sale newSale() {

			Sale sale = SaleBuilder.builder().id(1).shippedDate(SHIPPED_DATE).shippedBillNumber(SHIPPED_BILL_NUMBER).
					clientName(CLIENT_NAME).clientNumber(CLIENT_NUMBER).clientZip(ZIP).partNumber(PART_NUMBER).quantity(QUANTITY).
					serials(SERIALS).price(SALE_PRICE).ciscoType(CISCO_TYPE).comment(COMMENT).status(Sale.Status.NOT_PROCESSED).build();

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
	}

	public static class ClientsFactory {

		public static Client newClient() {

			return new Client(1L, CLIENT_NUMBER, PARTNER_NAME_FROM_PROVIDER, CLIENT_CITY, CLIENT_ADDRESS);
		}
	}

	public static class PromosFactory {

		public static Promo newPromo() {
			return new Promo(1L, PART_NUMBER, "2 Port Phone Adapter", PROMO_DISCOUNT, PROMO_CODE, GPL, PROMO_CODE, 5.52, 8, SHIPPED_DATE);
		}
	}

	public static class DartsFactory {

		public static Dart newDart() {
			return DartBuilder.builder().setId(1).setAuthorizationNumber(AUTHORIZATION_NUMBER).setVersion(1)
					.setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
					.setResellerName(PARTNER_NAME_FROM_PROVIDER).setResellerCountry("Ukraine").setResellerAcct(123)
					.setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(QUANTITY + 1)
					.setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
					.setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1)
					.build();
		}

		public static Dart newDart(String authorizationNumber, int quantity) {

			return DartBuilder.builder().setId(1).setAuthorizationNumber(authorizationNumber).setVersion(1)
					.setDistributorInfo(DISTRIBUTOR_INFO).setDistiDiscount(DART_DISTI_DISCOUNT)
					.setResellerName(PARTNER_NAME_FROM_PROVIDER).setResellerCountry("Ukraine").setResellerAcct(123)
					.setEndUserName(END_USER_NAME).setEndUserCountry("Country").setQuantity(quantity)
					.setQuantityInitial(QUANTITY + 1).setCiscoSku(PART_NUMBER).setDistiSku("Disti").setListPrice(1)
					.setClaimUnit(1).setExtCreditAmt(1).setFastTrackPie(1).setIpNgnPartnerPricingEm(1).setMdmFulfillment(1)
					.build();
		}
	}
}
