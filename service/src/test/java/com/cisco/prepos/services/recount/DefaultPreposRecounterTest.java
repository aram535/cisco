package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
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
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
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
    private static final int OTHER_GPL = 270;

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

    private final Prepos prepos = newPrepos();


    private final ImmutableMap<String, Promo> promosMap = ImmutableMap.of(PART_NUMBER, PromosFactory.newPromo());
    private final Dart selectedDart = DartsFactory.newDart(SELECTED_DART_AUTHORIZATION_NUMBER, SELECTED_DART_END_USER_NAME);
    private final Map<String, Dart> suitableDarts = createDarts();
    private Table<String, String, Dart> dartsTable = getDartsTable();
    private Pricelist newPricelist = newPricelist(OTHER_GPL);

    @Before
    public void init() {
        Map<String, Pricelist> pricelistMap = ImmutableMap.of(prepos.getPartNumber(), newPricelist);

        when(dartsService.getDartsTable()).thenReturn(dartsTable);
        when(promosService.getPromosMap()).thenReturn(promosMap);
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, dartsTable)).thenReturn(suitableDarts);
        when(dartSelector.selectDart(suitableDarts, prepos.getSecondPromo())).thenReturn(selectedDart);
        when(pricelistsService.getPricelistsMap()).thenReturn(pricelistMap);
        when(dartApplier.getPrepos(prepos, selectedDart, pricelistMap, dartsTable, promosMap)).thenReturn(newPrepos());
    }

    private Map<String, Dart> createDarts() {
        return ImmutableMap.of(AUTHORIZATION_NUMBER, selectedDart);
    }


    @Test
    public void thatReturnsEmptyListIfInputIsEmpty() {
        List<Triplet<Prepos, Map<String, Dart>, Dart>> recount = defaultPreposRecounter.recount(Lists.<Prepos>newArrayList());
        assertThat(recount).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsRecountedList() {
        List<Triplet<Prepos, Map<String, Dart>, Dart>> recountedPreposes = defaultPreposRecounter.recount(preposesList());

        assertThat(recountedPreposes).isNotNull().hasSize(1);

        Triplet<Prepos, Map<String, Dart>, Dart> expectedRecount = createExpectedTriplet();
        assertThat(recountedPreposes).contains(expectedRecount);
    }

    private Triplet<Prepos, Map<String, Dart>, Dart> createExpectedTriplet() {
        Prepos expectedPrepos = newPrepos();

        return new Triplet(expectedPrepos, suitableDarts, selectedDart);
    }

    private List<Prepos> preposesList() {
        return Lists.newArrayList(prepos);
    }
}
