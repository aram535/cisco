package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.junit.Test;

import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:23
 */

public class DefaultDiscountProviderTest {

	private static final String OTHER_PROMO = "other promo";

    private static final String OTHER_PART_NUMBER = "other part number";

    private DiscountProvider discountProvider = new DefaultDiscountProvider();

    @Test
    public void thatDiscountReturnsFromDartsIfExists() {
        Triplet<String, String, String> discountInfo = new Triplet<>(TestObjects.PART_NUMBER, null, TestObjects.SECOND_PROMO);

        double distiDiscount = discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap());

        assertThat(distiDiscount).isEqualTo(TestObjects.DART_DISTI_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPromosMapIfNoDartFound() {
        Triplet<String, String, String> discountInfo = new Triplet<>(TestObjects.PART_NUMBER, TestObjects.FIRST_PROMO, OTHER_PROMO);

        double distiDiscount = discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap());

        assertThat(distiDiscount).isEqualTo(TestObjects.PROMO_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPricelistMapIfNoDartAndPromoFound() {
        Triplet<String, String, String> discountInfo = new Triplet<>(TestObjects.PART_NUMBER, OTHER_PROMO, OTHER_PROMO);

        double distiDiscount = discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMapWithoutNeededPromo(), getPriceMap());

        assertThat(distiDiscount).isEqualTo(TestObjects.PRICE_LIST_DISCOUNT);
    }

    @Test(expected = CiscoException.class)
    public void thatThrowsCiscoExceptionIfNoPriceFound() {
        Triplet<String, String, String> discountInfo = new Triplet<>(OTHER_PART_NUMBER, OTHER_PROMO, OTHER_PROMO);


        discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap());
    }

    private Map<String, Pricelist> getPriceMap() {
        Map<String, Pricelist> map = Maps.newHashMap();
        Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setDiscount(TestObjects.PRICE_LIST_DISCOUNT).build();
        map.put(TestObjects.PART_NUMBER, pricelist);
        return map;
    }

    private Map<String, Dart> getSuitableDarts() {
        return Maps.<String, Dart>newHashMap();
    }

    private Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();

        Dart dart = DartBuilder.builder().setDistiDiscount(TestObjects.DART_DISTI_DISCOUNT).build();
        table.put(TestObjects.PART_NUMBER, TestObjects.SECOND_PROMO, dart);
        return table;
    }

    private Map<String, Promo> getPromosMapWithoutNeededPromo() {
        Map<String, Promo> map = Maps.newHashMap();
        Promo promo = PromoBuilder.newPromoBuilder().setDiscount(TestObjects.PROMO_DISCOUNT).build();
        map.put(OTHER_PART_NUMBER, promo);
        return map;
    }

    private Map<String, Promo> getPromosMap() {
        Map<String, Promo> map = Maps.newHashMap();
        Promo promo = PromoBuilder.newPromoBuilder().setDiscount(TestObjects.PROMO_DISCOUNT).build();
        map.put(TestObjects.PART_NUMBER, promo);
        return map;
    }

}
