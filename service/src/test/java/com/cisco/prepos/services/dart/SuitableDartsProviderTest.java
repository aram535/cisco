package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.Map;

import static com.cisco.testtools.TestObjects.*;
import static org.fest.assertions.api.Assertions.assertThat;

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
    public void thatReturnsEmptyListIfInputIsEmpty() {
        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, getEmptyDartsTable());
        assertThat(darts).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsEmptyIfPartNumberNotSuits() {
        Map<String, Dart> darts = suitableDartsProvider.getDarts(OTHER_PART_NUMBER, PARTNER_NAME, QUANTITY, DartsFactory.getDartsTable());
        assertThat(darts).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsDartsIfResellerNameAndQuantitySuits() {
        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();
        Dart expectedDart = dartsTableWithSuitableDart.get(PART_NUMBER, AUTHORIZATION_NUMBER);

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME, QUANTITY, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().hasSize(1);
        assertThat(darts).containsKey(AUTHORIZATION_NUMBER).containsValue(expectedDart);
    }

    @Test
    public void thatReturnsEmptyListIfResellerNameNotSuits() {
        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, OTHER_RESELLER_NAME, QUANTITY, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsEmptyListIfQuantityNotSuits() {
        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTable();

        Map<String, Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, OTHER_RESELLER_NAME, QUANTITY + 2, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().isEmpty();
    }

    private Table<String, String, Dart> getEmptyDartsTable() {
        return HashBasedTable.create();
    }

    private static Table<String, String, Dart> getDartsTable() {
        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = DartBuilder.builder().setResellerName(PARTNER_NAME).setQuantity(QUANTITY + 1).
                setAuthorizationNumber(AUTHORIZATION_NUMBER).build();
        table.put(PART_NUMBER, AUTHORIZATION_NUMBER, dart);
        return table;
    }

}
