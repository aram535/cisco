package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.promo.PromoValidator;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Quartet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.google.common.collect.ImmutableMap.of;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposRecounterTest {

    private static final String SELECTED_DART_AUTHORIZATION_NUMBER = "other authorization number";
    private static final String SELECTED_DART_END_USER_NAME = "other end user name";
    private static final Double OTHER_GPL = 270d;

    @InjectMocks
    private DefaultPreposRecounter defaultPreposRecounter = new DefaultPreposRecounter();

    @Mock
    private SuitableDartsProvider suitableDartsProvider;

    @Mock
    private DartSelector dartSelector;

    @Mock
    private DartsService dartsService;

    @Mock
    private PricelistsService pricelistsService;

    @Mock
    private PromosService promosService;

    @Mock
    private DartApplier dartApplier;

    @Mock
    private PromoValidator promoValidator;

    private final Prepos prepos = newPrepos();


    private final Promo promo = newPromo();
    private final Map<String, Promo> promosMap = of(PART_NUMBER, promo);
    private final Dart selectedDart = newDart(SELECTED_DART_AUTHORIZATION_NUMBER, SELECTED_DART_END_USER_NAME);
    private final Map<String, Dart> suitableDarts = createDarts();
    private Table<String, String, Dart> dartsTable = getDartsTable();
    private Pricelist newPricelist = newPricelist(OTHER_GPL);

    @Before
    public void init() {
        Map<String, Pricelist> pricelistMap = of(prepos.getPartNumber(), newPricelist);

        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(promosService.getPromosMap()).thenReturn(promosMap);
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, dartsTable)).thenReturn(suitableDarts);
        when(dartSelector.selectDart(suitableDarts, prepos.getSecondPromo())).thenReturn(selectedDart);
        when(pricelistsService.getPricelistsMap()).thenReturn(pricelistMap);
        when(dartApplier.getPrepos(prepos, selectedDart, pricelistMap, dartsTable, promosMap)).thenReturn(newPrepos());

        long shippedDateInMillis = prepos.getShippedDate().getTime();
        when(promoValidator.isValid(promo, shippedDateInMillis)).thenReturn(false);
    }

    private Map<String, Dart> createDarts() {
        return of(AUTHORIZATION_NUMBER, selectedDart);
    }


    @Test
    public void thatReturnsEmptyListIfInputIsEmpty() {
        List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recount = defaultPreposRecounter.recount(Lists.<Prepos>newArrayList());
        assertThat(recount).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsRecountedList() {
        List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recountedPreposes = defaultPreposRecounter.recount(preposesList());

        assertThat(recountedPreposes).isNotNull().hasSize(1);

        Quartet<Prepos, Map<String, Dart>, Dart, Boolean> expectedRecount = createExpectedTriplet();
        assertThat(recountedPreposes).contains(expectedRecount);
    }

    private Quartet<Prepos, Map<String, Dart>, Dart, Boolean> createExpectedTriplet() {
        Prepos expectedPrepos = newPrepos();

        return new Quartet(expectedPrepos, suitableDarts, selectedDart, false);
    }

    private List<Prepos> preposesList() {
        return Lists.newArrayList(prepos);
    }
}
