package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
public interface PromoRecounter {

    double getDiscount(PreposModel preposModel, Table<String, String, Dart> dartsTable, Map<String, Promo> promos, Map<String, Pricelist> priceMap);


}
