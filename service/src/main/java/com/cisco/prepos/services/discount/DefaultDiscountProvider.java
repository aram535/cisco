package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cisco.darts.dto.DartConstants.EMPTY_DART;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
@Component
public class DefaultDiscountProvider implements DiscountProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public double getDiscount(Dart selectedDart, Promo promo, Pricelist pricelist) {

        if (selectedDart != EMPTY_DART) {
            return selectedDart.getDistiDiscount();
        }

        if (promo != null) {
            return promo.getDiscount();
        }

        if (pricelist != null) {
            return pricelist.getDiscount();
        }

        logger.debug("NO price found");
        throw new CiscoException(String.format("NO price found"));
    }

    @Override
    public int getGpl(String partNumber, Map<String, Pricelist> pricelistsMap) {
        Pricelist pricelist = pricelistsMap.get(partNumber);

        if (pricelist == null) {
            throw new CiscoException(String.format("NO price found for part number %s", partNumber));
        }

        int gpl = pricelist.getGpl();

        return gpl;
    }


    @Override
    public boolean isRelevant(Promo existingPromo) {
        return true;
    }


}
