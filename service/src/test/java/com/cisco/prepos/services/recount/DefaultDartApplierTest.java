package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;
import static com.cisco.testtools.TestObjects.DartsFactory;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.google.common.collect.ImmutableMap.of;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
    private static final Double OTHER_GPL = 270d;
    private static final double SALE_DISCOUNT_FROM_PROVIDER = 0.26;
    private static final double BUY_DISCOUNT_FROM_PROVIDER = 0.38;
    private static final double BUY_PRICE = 167.4;
    private static final double THRESHOLD = 1.27;

    private final Prepos prepos = newPrepos();
    private final Dart selectedDart = DartsFactory.newDart(SELECTED_DART_AUTHORIZATION_NUMBER, SELECTED_DART_END_USER_NAME);
    private final Pricelist newPricelist = newPricelist(OTHER_GPL);
    private final Promo promo = newPromo("other promo code");
    private final Table<String, String, Dart> dartsTable = getDartsTable();
    private Map<String, Pricelist> pricelistMap = of(prepos.getPartNumber(), newPricelist);
    private final Map<String, Promo> promosMap = of(PART_NUMBER, promo);


    @InjectMocks
    private DefaultDartApplier defaultDartApplier = new DefaultDartApplier();

    @Mock
    private DiscountProvider discountProvider;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
    public void init() {
        when(discountProvider.getGpl(PART_NUMBER, pricelistMap)).thenReturn(OTHER_GPL);

        long shippedDateInMillis = prepos.getShippedDate().getTime();
        when(discountProvider.getDiscount(selectedDart, promo, newPricelist, shippedDateInMillis)).thenReturn(BUY_DISCOUNT_FROM_PROVIDER);

        defaultDartApplier.setThreshold(THRESHOLD);
    }

    @Test
    public void thatSelectedDartAppliedCorrectly() {
        Prepos result = defaultDartApplier.getPrepos(prepos, selectedDart, pricelistMap, dartsTable, promosMap);
        assertThat(result).isEqualTo(createExpectedPrepos());
    }

	@Test
	public void thatDartQuantityUpdatedCorrectlyForPreposesList() {

		Prepos expectedPrepos = newPrepos();
		expectedPrepos.setQuantity(1);

		List<Dart> darts = defaultDartApplier.updateDartQuantity(Lists.newArrayList(expectedPrepos), dartsTable);

		Dart dart = Iterables.getOnlyElement(darts);
		assertEquals(0, dart.getQuantity());
	}

	@Test
	public void thatExceptionIsThrownWhenNotEnoughDartQuantity() throws Exception {

		Prepos expectedPrepos = newPrepos();
		expectedPrepos.setQuantity(2);

		Dart dart = dartsTable.get(expectedPrepos.getPartNumber(), expectedPrepos.getSecondPromo());

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage(String.format(
				"Not enough available quantity for Dart with PN:%s and AN:%s. Please contact support",
				dart.getCiscoSku(), dart.getAuthorizationNumber()));

		defaultDartApplier.updateDartQuantity(Lists.newArrayList(expectedPrepos), dartsTable);

	}

	@Test
	public void thatExceptionIsThrownWhenNoPricelistFoundForPartNumber() throws Exception {

		pricelistMap = of();
		Prepos expectedPrepos = newPrepos();

		expectedException.expect(CiscoException.class);
		expectedException.expectMessage(String.format("NO price found for part number:%s", expectedPrepos.getPartNumber()));

		defaultDartApplier.validateIfPricelistsExistsForPreposes(Lists.newArrayList(expectedPrepos), pricelistMap);

	}

	@Test
	public void thatNoExceptionIsThrownWhenPricelistIsFoundForPartNumber() throws Exception {

		Prepos expectedPrepos = newPrepos();

		defaultDartApplier.validateIfPricelistsExistsForPreposes(Lists.newArrayList(expectedPrepos), pricelistMap);
	}

	private Prepos createExpectedPrepos() {
        Prepos expectedPrepos = newPrepos();
        expectedPrepos.setFirstPromo(promo.getCode());
        expectedPrepos.setSecondPromo(SELECTED_DART_AUTHORIZATION_NUMBER);
        expectedPrepos.setEndUser(SELECTED_DART_END_USER_NAME);
        expectedPrepos.setSaleDiscount(SALE_DISCOUNT_FROM_PROVIDER);
        expectedPrepos.setBuyDiscount(BUY_DISCOUNT_FROM_PROVIDER);
        expectedPrepos.setBuyPrice(BUY_PRICE);
	    expectedPrepos.setDelta((int) ((TestObjects.SALE_PRICE / BUY_PRICE * 100) - 100));
        expectedPrepos.setPosSum(getRoundedDouble(BUY_PRICE * expectedPrepos.getQuantity()));

        boolean isOk = expectedPrepos.getSalePrice() / expectedPrepos.getBuyPrice() > THRESHOLD;
        expectedPrepos.setOk(isOk);

        return expectedPrepos;
    }
}
