package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.google.common.collect.Table;

import java.util.List;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:33
 */
public interface SuitableDartsProvider {

    List<Dart> getDarts(String partNumber, String partnerName, int quantity, Table<String, String, Dart> dartsTable);

}
