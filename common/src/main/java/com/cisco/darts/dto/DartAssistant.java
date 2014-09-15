package com.cisco.darts.dto;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Rost
 * Date: 14.05.2014
 * Time: 1:49
 */
public class DartAssistant {
    public static final String BLANK_AUTHORIZATION_NUMBER = "";
    public static final String BLANK_END_USER_NAME = "";
    public static final Dart EMPTY_DART = new Dart(BLANK_AUTHORIZATION_NUMBER, BLANK_END_USER_NAME);

    public static Table<String, String, Dart> dartsToTable(Iterable<Dart> darts) {

        Table<String, String, Dart> dartsTable = HashBasedTable.create();

        for (Dart dart : darts) {
            dartsTable.put(dart.getCiscoSku(), dart.getAuthorizationNumber(), dart);
        }

        return dartsTable;
    }

    public static List<Dart> tableToList(Table<String, String, Dart> table) {

        if (table == null) {
            return Lists.newArrayList();
        }

        List<Dart> dartList = newArrayList();
        for (String authNumber : table.columnKeySet()) {
            Map<String, Dart> dartMap = table.column(authNumber);
            dartList.addAll(dartMap.values());
        }

        return dartList;
    }
}
