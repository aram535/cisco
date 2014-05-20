package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import org.fest.assertions.api.Assertions;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 20.05.2014
 * Time: 21:29
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDartApplierTest {

    private static final String SELECTED_DART_AUTHORIZATION_NUMBER = "other authorization number";
    private static final String SELECTED_DART_END_USER_NAME = "other end user name";
    private static final int OTHER_GPL = 270;
    private static final double SALE_DISCOUNT_FROM_PROVIDER = 0.26;
    private static final double BUY_DISCOUNT_FROM_PROVIDER = 0.38;
    private static final double BUY_PRICE = 167.4;
    private static final double THRESHOLD = 1.27;

    private final Prepos prepos = newPrepos();
    private final Dart selectedDart = TestObjects.DartsFactory.newDart(SELECTED_DART_AUTHORIZATION_NUMBER, SELECTED_DART_END_USER_NAME);
    private Pricelist newPricelist = newPricelist(OTHER_GPL);
    private Table<String, String, Dart> dartsTable = getDartsTable();
    private Map<String, Pricelist> pricelistMap = ImmutableMap.of(prepos.getPartNumber(), newPricelist);
    private final ImmutableMap<String, Promo> promosMap = ImmutableMap.of(PART_NUMBER, TestObjects.PromosFactory.newPromo());


    @InjectMocks
    private DefaultDartApplier defaultDartApplier = new DefaultDartApplier();

    @Mock
    private DiscountProvider discountProvider;

    @Before
    public void init() {
        Triplet<String, String, String> discountInfo = new Triplet(prepos.getPartNumber(), prepos.getFirstPromo(), SELECTED_DART_AUTHORIZATION_NUMBER);
        when(discountProvider.getGpl(PART_NUMBER, pricelistMap)).thenReturn(OTHER_GPL);
        when(discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistMap)).thenReturn(BUY_DISCOUNT_FROM_PROVIDER);
        defaultDartApplier.setThreshold(THRESHOLD);
    }

    @Test
    public void thatSelectedDartAppliedCorrectly() {
        Prepos result = defaultDartApplier.getPrepos(prepos, selectedDart, pricelistMap, dartsTable, promosMap);
        Assertions.assertThat(result).isEqualTo(createExpectedPrepos());
    }

    private Prepos createExpectedPrepos() {
        Prepos expectedPrepos = newPrepos();
        expectedPrepos.setSecondPromo(SELECTED_DART_AUTHORIZATION_NUMBER);
        expectedPrepos.setEndUser(SELECTED_DART_END_USER_NAME);
        expectedPrepos.setSaleDiscount(SALE_DISCOUNT_FROM_PROVIDER);
        expectedPrepos.setBuyDiscount(BUY_DISCOUNT_FROM_PROVIDER);
        expectedPrepos.setBuyPrice(BUY_PRICE);
        expectedPrepos.setPosSum(getRoundedDouble(BUY_PRICE * expectedPrepos.getQuantity()));

        boolean isOk = expectedPrepos.getSalePrice() / expectedPrepos.getBuyPrice() > THRESHOLD;
        expectedPrepos.setOk(isOk);

        return expectedPrepos;
    }
}
