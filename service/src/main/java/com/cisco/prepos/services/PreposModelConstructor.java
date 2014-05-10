package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:09
 */
public interface PreposModelConstructor {

    List<PreposModel> construct(List<Prepos> preposes, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable);
}
