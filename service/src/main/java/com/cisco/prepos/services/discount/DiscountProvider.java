package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
public interface DiscountProvider {

    double getDiscount(Dart selectedDart, Promo promo, Pricelist pricelist, long shippedDateInMillis);

    int getGpl(String partNumber, Map<String, Pricelist> pricelistsMap);

}
