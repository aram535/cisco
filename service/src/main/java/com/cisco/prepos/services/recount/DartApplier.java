package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 20.05.2014
 * Time: 21:24
 */
public interface DartApplier {
    Prepos getPrepos(Prepos inputPrepos, Dart selectedDart, Map<String, Pricelist> pricelistsMap, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap);

	List<Dart> updateDartQuantity(List<Prepos> preposes,  Table<String, String, Dart> dartsTable);

	void validateIfPricelistsExistsForPreposes(List<Prepos> preposes, Map<String, Pricelist> pricelistsMap);
}
