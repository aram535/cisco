package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
@Component
public class DefaultDiscountProvider implements DiscountProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public double getDiscount(Prepos prepos, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap, Map<String, Pricelist> priceMap) {

        String partNumber = prepos.getPartNumber();

        if (prepos.getSecondPromo() != null) {

            Dart dart = dartsTable.get(partNumber,
                    prepos.getSecondPromo());

            if (dart != null) {
                return dart.getDistiDiscount();
            }
        }

        String firstPromo = prepos.getFirstPromo();

        if (firstPromo != null) {
            Promo promo = promosMap.get(partNumber);
            if (promo != null) {
                return promo.getDiscount();
            }
        }

        Pricelist pricelist = priceMap.get(partNumber);
        if (pricelist != null) {
            return pricelist.getDiscount();
        }

        logger.debug("NO price found for part number {}", partNumber);
        throw new CiscoException(String.format("NO price found for part number %s", partNumber));
    }


}
