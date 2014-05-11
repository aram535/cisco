package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.*;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.model.PreposModel.EMPTY_DART;
import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PreposFactory.*;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 05.05.2014
 * Time: 0:31
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposModelConstructorTest {

    @InjectMocks
    private PreposModelConstructor preposModelConstructor = new DefaultPreposModelConstructor();

    @Mock
    private DiscountProvider discountProvider;

    @Mock
    private SuitableDartsProvider suitableDartsProvider;

    private List<Prepos> preposes;

    @Before
    public void init() {

	    preposModelConstructor.setTreshhold(1.27);

	    preposes = Lists.newArrayList(newSimplePrepos());

        Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
        when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(BUY_DISCOUNT);
	    when(discountProvider.isRelevant((Promo) any())).thenReturn(true);
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, getDartsTable())).thenReturn(getDartsTable().row(PART_NUMBER));
    }


	@Test
	public void thatConstructsModelWithEmptySelectedDartIfThereIsNoSuitable() {

		when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, getDartsTable())).thenReturn(Maps.<String, Dart>newHashMap());

		List<PreposModel> preposModels = preposModelConstructor.construct(preposes, getPriceMap(), getPromosMap(), getDartsTable());

		Map<String, Dart> suitableDarts = Iterables.getOnlyElement(preposModels).getSuitableDarts();

		assertThat(suitableDarts).isNotNull();
		assertThat(suitableDarts.size()).isEqualTo(1);
		assertThat(suitableDarts).contains(entry("", EMPTY_DART));
		assertThat(Iterables.getOnlyElement(preposModels).getSelectedDart()).isEqualTo(EMPTY_DART);
	}

	@Test
	public void thatConstructsModelWithCorrectPricesDiscountsAndSuitableDarts() {

		Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
		when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(DART_DISTI_DISCOUNT);
		when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, getDartsTable())).thenReturn(getDartsTable().row(PART_NUMBER));


		List<PreposModel> preposModels = preposModelConstructor.construct(preposes, getPriceMap(), getPromosMap(), getDartsTable());

		Dart expectedFirstDart = getDartsTable().row(PART_NUMBER).entrySet().iterator().next().getValue();
		expectedFirstDart.setQuantity(1);

		Prepos expectedPrepos = newPrepos();
		expectedPrepos.setBuyDiscount(DART_DISTI_DISCOUNT);
		expectedPrepos.setBuyPrice(DART_DISTI_PRICE);


		assertThat(Iterables.getOnlyElement(preposModels).getPrepos()).isEqualTo(expectedPrepos);
		assertThat(Iterables.getOnlyElement(preposModels).getSuitableDarts().size()).isEqualTo(3);
		assertThat(Iterables.getOnlyElement(preposModels).getSelectedDart()).isEqualTo(expectedFirstDart);

	}

    @Test
    public void thatConstructsEmptyModelsListIfPreposesAreEmpty() {
        List<PreposModel> preposModels = preposModelConstructor.construct(Lists.<Prepos>newArrayList(), getPriceMap(), getPromosMap(), getDartsTable());
        assertThat(preposModels).isNotNull().isEmpty();
    }

	@Test
	public void thatPreposModelBeingCorrectlyConvertedToPreposes() {

		List<PreposModel> preposModels = getNewPreposModels();

		List<Prepos> preposes = preposModelConstructor.getPreposesFromPreposModels(preposModels);

		assertThat(preposes).isEqualTo(newPreposList());
	}

	@Test
	public void thatForPreposWithExistingEmptyDartSelectedDartNotBeingChanged() {
		Prepos prepos = newPrepos();
		prepos.setSecondPromo("");
		List<Prepos> preposes = Lists.newArrayList(prepos);

		List<PreposModel> preposModels = preposModelConstructor.construct(preposes, getPriceMap(), getPromosMap(), getDartsTable());

		assertThat(Iterables.getOnlyElement(preposModels).getSelectedDart()).isEqualTo(EMPTY_DART);
	}

	@Test
	public void thatRecountsPriceCorrectlyWithDiscountFromNewNonEmptySelectedDart() {

		Prepos prepos = newPrepos();
		Map<String, Dart> suitableDarts = ImmutableMap.of("", PreposModel.EMPTY_DART);
		PreposModel preposModel = new PreposModel(prepos, suitableDarts, PreposModel.EMPTY_DART);

		Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
		when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(DART_DISTI_DISCOUNT);

		preposModelConstructor.recountPreposPrices(preposModel, getPriceMap(), getPromosMap(), getDartsTable());

		assertThat(preposModel.getPrepos().getBuyDiscount()).isEqualTo(DART_DISTI_DISCOUNT);
		assertThat(preposModel.getPrepos().getBuyPrice()).isEqualTo(DART_DISTI_PRICE);
		assertThat(preposModel.getPrepos().getOk()).isTrue();

	}

	@Test
	public void thatRecountsPriceCorrectlyWithDiscountFromFirstPromoIfSelectedDartIsEmpty() {

		Prepos prepos = newPrepos();
		Map<String, Dart> suitableDarts = ImmutableMap.of("", PreposModel.EMPTY_DART);
		PreposModel preposModel = new PreposModel(prepos, suitableDarts, PreposModel.EMPTY_DART);

		Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
		when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(PROMO_DISCOUNT);
		preposModelConstructor.setTreshhold(1.25);
		preposModelConstructor.recountPreposPrices(preposModel, getPriceMap(), getPromosMap(), getDartsTable());

		assertThat(preposModel.getPrepos().getBuyDiscount()).isEqualTo(PROMO_DISCOUNT);
		assertThat(preposModel.getPrepos().getBuyPrice()).isEqualTo(PROMO_PRICE);
		assertThat(preposModel.getPrepos().getOk()).isFalse();
	}

	@Test
	public void thatRecountsPriceCorrectlyWithDiscountFromPricelistIfSelectedDartAndFirstIsEmpty() {

		Prepos prepos = newPrepos();
		Map<String, Dart> suitableDarts = ImmutableMap.of("", PreposModel.EMPTY_DART);
		PreposModel preposModel = new PreposModel(prepos, suitableDarts, PreposModel.EMPTY_DART);

		Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
		when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(PRICE_LIST_DISCOUNT);
		preposModelConstructor.setTreshhold(1.25);
		preposModelConstructor.recountPreposPrices(preposModel, getPriceMap(), getPromosMap(), getDartsTable());

		assertThat(preposModel.getPrepos().getBuyDiscount()).isEqualTo(PRICE_LIST_DISCOUNT);
		assertThat(preposModel.getPrepos().getBuyPrice()).isEqualTo(PRICE_LIST_PRICE);
		assertThat(preposModel.getPrepos().getOk()).isFalse();
	}

    private Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(SHIPPED_DATE);
	    cal.add(Calendar.DAY_OF_WEEK, 1);

	    Timestamp endDate = new Timestamp(cal.getTime().getTime());

	    cal.add(Calendar.DAY_OF_WEEK, -2);
	    Timestamp startDate = new Timestamp(cal.getTime().getTime());

        table.put(PART_NUMBER, AUTHORIZATION_NUMBER, newDart(startDate, endDate));
	    table.put(PART_NUMBER, AUTHORIZATION_NUMBER + 1, newDart(startDate, endDate));

        return table;
    }

    private Map<String, Promo> getPromosMap() {
        Map<String, Promo> map = Maps.newHashMap();

	    map.put(PART_NUMBER, newPromo());

        return map;
    }

    private Map<String, Pricelist> getPriceMap() {
        Map<String, Pricelist> map = Maps.newHashMap();

        map.put(PART_NUMBER, newPricelist());

        return map;
    }

	private List<PreposModel> getNewPreposModels() {
		Prepos prepos = newPrepos();
		Map<String, Dart> suitableDarts = ImmutableMap.of("", PreposModel.EMPTY_DART);
		PreposModel preposModel = new PreposModel(prepos, suitableDarts,PreposModel.EMPTY_DART);

		return Lists.newArrayList(preposModel);
	}
}
