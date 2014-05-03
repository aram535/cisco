package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 19:22
 */
public class DefaultPromoRecounter implements PromoRecounter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public double getDiscount(PreposModel preposModel, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap, Map<String, Pricelist> priceMap) {

        Prepos prepos = preposModel.getPrepos();

        if (prepos.getSecondPromo() != null) {

            Dart dart = dartsTable.get(prepos.getPartNumber(),
                    prepos.getSecondPromo());

            if (dart != null) {
                return dart.getDistiDiscount();
            }
        }

        String firstPromo = prepos.getFirstPromo();

        if (firstPromo != null) {
            Promo promo = promosMap.get(firstPromo);
            if (promo != null) {
                return promo.getDiscount();
            }
        }

        Pricelist pricelist = priceMap.get(prepos.getPartNumber());
        if (pricelist != null) {
            return pricelist.getDiscount();
        }

        logger.debug("NO discount found for prepos {}", prepos);
        return 0;
    }


}
