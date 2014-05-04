package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
public interface DiscountProvider {

    double getDiscount(Prepos prepos, Table<String, String, Dart> dartsTable, Map<String, Promo> promos, Map<String, Pricelist> priceMap);


}
