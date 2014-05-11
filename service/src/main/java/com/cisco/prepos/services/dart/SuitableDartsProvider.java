package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.google.common.collect.Table;

import java.sql.Timestamp;
import java.util.Map;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:33
 */
public interface SuitableDartsProvider {

    Map<String, Dart> getDarts(String partNumber, String partnerName, int quantity, Timestamp saleDate, Table<String, String, Dart> dartsTable);

}
