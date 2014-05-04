package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.javatuples.Triplet;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
public interface DiscountProvider {

    double getDiscount(Triplet<String, String, String> discountInfo, Table<String, String, Dart> dartsTable, Map<String, Promo> promos, Map<String, Pricelist> priceMap);


}
