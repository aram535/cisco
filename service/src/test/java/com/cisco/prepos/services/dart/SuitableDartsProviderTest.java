package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import static com.cisco.darts.dto.DartConstants.BLANK_AUTHORIZATION_NUMBER;
import static com.cisco.darts.dto.DartConstants.EMPTY_DART;
import static com.cisco.testtools.TestObjects.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:35
 */
public class SuitableDartsProviderTest {

    private static final String OTHER_PART_NUMBER = "other part number";
    private static final String OTHER_RESELLER_NAME = "other reseller name";
    private SuitableDartsProvider suitableDartsProvider = new DefaultSuitableDartsProvider();

    @Test
    public void thatReturnsMapWithEmptyDartIfInputIsEmpty() {

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, getEmptyDartsTable());
        assertThat(darts).isNotNull().isEqualTo(mapWithEmptyDart());
    }

    @Test
    public void thatReturnsMapWithEmptyDartIfPartNumberNotSuits() {

        Map<String, Dart> darts = suitableDartsProvider.getDarts(OTHER_PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, DartsFactory.getDartsTable());
        assertThat(darts).isNotNull().isEqualTo(mapWithEmptyDart());
    }

    @Test
    public void thatReturnsDartsIfResellerNameAndQuantitySuits() {

        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();
        Dart expectedDart = dartsTableWithSuitableDart.get(PART_NUMBER, AUTHORIZATION_NUMBER);

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, SHIPPED_DATE, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().hasSize(2);
        assertThat(darts).contains(entry(AUTHORIZATION_NUMBER, expectedDart), entry(BLANK_AUTHORIZATION_NUMBER, EMPTY_DART));
    }

    @Test
    public void thatReturnsMapWithEmptyDartListIfResellerNameNotSuits() {

        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, OTHER_RESELLER_NAME, QUANTITY, SHIPPED_DATE, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().isEqualTo(mapWithEmptyDart());
    }

    @Test
    public void thatReturnsMapWithEmptyDartIfQuantityNotSuits() {

        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, OTHER_RESELLER_NAME, QUANTITY + 2, SHIPPED_DATE, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().isEqualTo(mapWithEmptyDart());
    }

    private Table<String, String, Dart> getEmptyDartsTable() {

        return HashBasedTable.create();
    }

    private Map<String, Dart> mapWithEmptyDart() {
        return ImmutableMap.of(BLANK_AUTHORIZATION_NUMBER, EMPTY_DART);
    }

    private Table<String, String, Dart> getDartsTable() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(SHIPPED_DATE);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        Timestamp endDate = new Timestamp(cal.getTime().getTime());

        cal.add(Calendar.DAY_OF_WEEK, -2);
        Timestamp startDate = new Timestamp(cal.getTime().getTime());

        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = DartBuilder.builder().setResellerName(PARTNER_NAME).setQuantity(QUANTITY + 1).
                setAuthorizationNumber(AUTHORIZATION_NUMBER).setStartDate(startDate).setEndDate(endDate).build();
        table.put(PART_NUMBER, AUTHORIZATION_NUMBER, dart);

        return table;
    }

}
