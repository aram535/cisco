package com.cisco.prepos.services.discount;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
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
    public double getDiscount(Triplet<String, String, String> discountInfo, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap, Map<String, Pricelist> priceMap) {

        String partNumber = discountInfo.getValue0();
        String firstPromo = discountInfo.getValue1();
        String secondPromo = discountInfo.getValue2();

        if (secondPromo != null) {

            Dart dart = dartsTable.get(partNumber,
                    secondPromo);

            if (dart != null) {
                return dart.getDistiDiscount();
            }
        }

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
