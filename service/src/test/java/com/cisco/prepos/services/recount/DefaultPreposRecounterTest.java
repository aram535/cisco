package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
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

import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.PreposFactory.newPreposList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposRecounterTest {

    @InjectMocks
    DefaultPreposRecounter defaultPreposRecounter = new DefaultPreposRecounter();

    @Mock
    private SuitableDartsProvider suitableDartsProvider;

    @Before
    public void init() {
        when(suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, createDartsTable())).thenReturn(createDarts());
    }

    private Map<String, Dart> createDarts() {
        return null;
    }

    private Table<String, String, Dart> createDartsTable() {
        return getDartsTable();
    }

    @Test
    public void thatReturnsEmptyListIfInputIsEmpty() {
        List<Triplet<Prepos, Map<String, Dart>, Dart>> recount = defaultPreposRecounter.recount(Lists.<Prepos>newArrayList());
        assertThat(recount).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsRecountedList() {
        List<Triplet<Prepos, Map<String, Dart>, Dart>> recount = defaultPreposRecounter.recount(preposesList());
    }

    private List<Prepos> preposesList() {
        return newPreposList();
    }
}
