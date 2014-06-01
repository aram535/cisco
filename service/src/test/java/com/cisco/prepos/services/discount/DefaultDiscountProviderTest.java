package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Map;

import static com.cisco.darts.dto.DartConstants.EMPTY_DART;
import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:23
 */

public class DefaultDiscountProviderTest {

    private static final String OTHER_PART_NUMBER = "other part number";
    private static final int GPL = 270;


    private DiscountProvider discountProvider = new DefaultDiscountProvider();
    private final Dart dart = DartBuilder.builder().setDistiDiscount(DART_DISTI_DISCOUNT).build();
    private final Promo promo = PromoBuilder.newPromoBuilder().setDiscount(PROMO_DISCOUNT).build();
    private final Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setDiscount(PRICE_LIST_DISCOUNT).setGpl(GPL).build();

    @Test
    public void thatDiscountReturnsFromDartsIfExists() {
        double distiDiscount = discountProvider.getDiscount(dart, promo, pricelist);
        assertThat(distiDiscount).isEqualTo(DART_DISTI_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPromoIfDartIsEmpty() {

        double distiDiscount = discountProvider.getDiscount(EMPTY_DART, promo, pricelist);

        assertThat(distiDiscount).isEqualTo(PROMO_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPricelistIfDartIsEmptyAndPromoIsNull() {
        double distiDiscount = discountProvider.getDiscount(EMPTY_DART, null, pricelist);

        assertThat(distiDiscount).isEqualTo(PRICE_LIST_DISCOUNT);
    }

    @Test(expected = CiscoException.class)
    public void thatThrowsCiscoExceptionIfNoPriceFound() {
        discountProvider.getDiscount(EMPTY_DART, null, null);
    }

    @Test(expected = CiscoException.class)
    public void thatOnGetGplThrowsCiscoExceptionIfNoPriceFound() {
        discountProvider.getGpl(OTHER_PART_NUMBER, getPriceMap());
    }

    @Test
    public void thatOnGetGplReturnsGplAccordingToPricelist() {
        double gpl = discountProvider.getGpl(PART_NUMBER, getPriceMap());
        assertThat(gpl).isEqualTo(GPL);
    }

    private Map<String, Pricelist> getPriceMap() {
        Map<String, Pricelist> map = Maps.newHashMap();
        Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setDiscount(PRICE_LIST_DISCOUNT).setGpl(GPL).build();
        map.put(PART_NUMBER, pricelist);
        return map;
    }


}
