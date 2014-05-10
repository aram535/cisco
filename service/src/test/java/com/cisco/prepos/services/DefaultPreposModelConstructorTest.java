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

import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartBuilder.builder;
import static com.cisco.prepos.model.PreposModel.EMPTY_DART;
import static com.cisco.pricelists.dto.PricelistBuilder.newPricelistBuilder;
import static com.cisco.promos.dto.PromoBuilder.newPromoBuilder;
import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.PreposFactory.newSimplePrepos;
import static org.fest.assertions.api.Assertions.assertThat;
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

	    preposes = Lists.newArrayList(newSimplePrepos());

        Triplet<String, String, String> discountInfo = new Triplet(PART_NUMBER, PROMO_CODE, AUTHORIZATION_NUMBER);
        when(discountProvider.getDiscount(discountInfo, getDartsTable(), getPromosMap(), getPriceMap())).thenReturn(BUY_DISCOUNT);
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, getDartsTable())).thenReturn(getDartsTable().row(PART_NUMBER));
    }


	@Test
	public void thatConstructsModelWithEmptySelectedDartIfThereIsNoSuitable() {

		when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, getDartsTable())).thenReturn(Maps.<String, Dart>newHashMap());

		List<PreposModel> preposModels = preposModelConstructor.construct(preposes, getPriceMap(), getPromosMap(), getDartsTable());

		Map<String, Dart> suitableDarts = Iterables.getOnlyElement(preposModels).getSuitableDarts();
		assertThat(suitableDarts).isNotNull().isEmpty();
		assertThat(Iterables.getOnlyElement(preposModels).getSelectedDart()).isEqualTo(EMPTY_DART);
	}

	@Test
	public void thatConstructsModelWithCorrectPricesDiscountsAndSuitableDarts() {

		List<PreposModel> preposModels = preposModelConstructor.construct(preposes, getPriceMap(), getPromosMap(), getDartsTable());

		Map<String, Dart> expectedSuitableDarts = getDartsTable().row(PART_NUMBER);
		Dart expectedFirstDart = expectedSuitableDarts.entrySet().iterator().next().getValue();

		//assertThat(Iterables.getOnlyElement(preposModels).getPrepos()).isEqualTo(Iterables.getOnlyElement(newPreposList()));
		assertThat(Iterables.getOnlyElement(preposModels).getSuitableDarts()).isEqualTo(expectedSuitableDarts);
		assertThat(Iterables.getOnlyElement(preposModels).getSelectedDart()).isEqualTo(expectedFirstDart);
	}

    @Test
    public void thatConstructsEmptyModelsListIfPreposesAreEmpty() {
        List<PreposModel> preposModels = preposModelConstructor.construct(Lists.<Prepos>newArrayList(), getPriceMap(), getPromosMap(), getDartsTable());
        assertThat(preposModels).isNotNull().isEmpty();
    }

    private Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = builder().build();
        table.put(PART_NUMBER, AUTHORIZATION_NUMBER, dart);
	    table.put(PART_NUMBER, AUTHORIZATION_NUMBER + 1, dart);
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
