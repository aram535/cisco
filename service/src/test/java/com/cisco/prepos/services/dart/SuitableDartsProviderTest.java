package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.List;

import static com.cisco.testtools.TestObjects.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:35
 */
public class SuitableDartsProviderTest {

    private static final String OTHER_PART_NUMBER = "other part number";
    private SuitableDartsProvider suitableDartsProvider = new DefaultSuitableDartsProvider();

    @Test
    public void thatReturnsEmptyListIfInputIsEmpty() {
        List<Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME_FROM_PROVIDER, QUANTITY, getEmptyDartsTable());
        assertThat(darts).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsEmptyIfPartNumberNotSuits() {
        List<Dart> darts = suitableDartsProvider.getDarts(OTHER_PART_NUMBER, PARTNER_NAME_FROM_PROVIDER, QUANTITY, DartsFactory.getDartsTable());
        assertThat(darts).isNotNull().isEmpty();
    }

    @Test
    public void thatReturnsDartsIfResellerNameAndQuantitySuits() {
        Table<String, String, Dart> dartsTableWithSuitableDart = getDartsTableWithSuitableDart();
        Dart expectedDart = dartsTableWithSuitableDart.get(PART_NUMBER, SECOND_PROMO);

        List<Dart> darts = suitableDartsProvider.getDarts(PART_NUMBER, PARTNER_NAME_FROM_PROVIDER, QUANTITY, dartsTableWithSuitableDart);

        assertThat(darts).isNotNull().hasSize(1);
        assertThat(darts).contains(expectedDart);
    }

    private Table<String, String, Dart> getEmptyDartsTable() {
        return HashBasedTable.create();
    }

    public static Table<String, String, Dart> getDartsTableWithSuitableDart() {
        Table<String, String, Dart> table = HashBasedTable.create();
        Dart dart = DartBuilder.builder().setResellerName(PARTNER_NAME_FROM_PROVIDER).setQuantity(QUANTITY + 1).build();
        table.put(PART_NUMBER, SECOND_PROMO, dart);
        return table;
    }

}
