package com.cisco.prepos.services.promo;

import com.cisco.promos.dto.Promo;
import org.springframework.stereotype.Component;

/**
 * User: Rost
 * Date: 01.06.2014
 * Time: 17:16
 */
@Component
public class DefaultPromoValidator implements PromoValidator {

    @Override
    public boolean isValid(Promo promo, long shippedDateInMillis) {

        if (promo != null) {
            long promoEndDateInMillis = promo.getEndDate().getTime();
            return shippedDateInMillis < promoEndDateInMillis;
        }

        return false;
    }
}
