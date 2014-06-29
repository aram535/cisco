package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.services.promo.PromoValidator;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.cisco.darts.dto.DartAssistant.EMPTY_DART;
import static com.cisco.testtools.TestObjects.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:23
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDiscountProviderTest {

    private static final String OTHER_PART_NUMBER = "other part number";
    private static final int GPL = 270;
    private final long shippedDateInMillis = 500L;

    @InjectMocks
    private DiscountProvider discountProvider = new DefaultDiscountProvider();

    @Mock
    private PromoValidator promoValidator;

    private final Dart dart = DartBuilder.builder().setDistiDiscount(DART_DISTI_DISCOUNT).build();
    private final Promo promo = PromoBuilder.newPromoBuilder().setDiscount(PROMO_DISCOUNT).build();
    private final Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setDiscount(PRICE_LIST_DISCOUNT).setGpl(GPL).build();


    @Before
    public void init(){
        when(promoValidator.isValid(promo, shippedDateInMillis)).thenReturn(true);
        when(promoValidator.isValid(null, shippedDateInMillis)).thenReturn(false);
    }

    @Test
    public void thatDiscountReturnsFromDartsIfExists() {
        double distiDiscount = discountProvider.getDiscount(dart, promo, pricelist, shippedDateInMillis);
        assertThat(distiDiscount).isEqualTo(DART_DISTI_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPromoIfDartIsEmptyAndPromoIsValid() {
        double distiDiscount = discountProvider.getDiscount(EMPTY_DART, promo, pricelist, shippedDateInMillis);

        assertThat(distiDiscount).isEqualTo(PROMO_DISCOUNT);
    }

    @Test
    public void thatDiscountReturnsFromPricelistIfDartIsEmptyAndPromoIsNotValid() {
        double distiDiscount = discountProvider.getDiscount(EMPTY_DART, null, pricelist, shippedDateInMillis);

        assertThat(distiDiscount).isEqualTo(PRICE_LIST_DISCOUNT);
    }

    @Test(expected = CiscoException.class)
    public void thatThrowsCiscoExceptionIfNoPriceFound() {
        discountProvider.getDiscount(EMPTY_DART, null, null, shippedDateInMillis);
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
