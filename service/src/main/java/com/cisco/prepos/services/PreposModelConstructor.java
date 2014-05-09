package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.Table;

import java.util.List;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:09
 */
public interface PreposModelConstructor {

    List<PreposModel> constructPreposModels(List<Prepos> preposes, Table<String, String, Dart> dartsTable);
}
