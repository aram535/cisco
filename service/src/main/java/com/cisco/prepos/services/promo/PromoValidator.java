package com.cisco.prepos.services.promo;

import com.cisco.promos.dto.Promo;

/**
 * User: Rost
 * Date: 01.06.2014
 * Time: 17:06
 */
public interface PromoValidator {
    boolean isValid(Promo promo, long shippedDateInMillis);
}
