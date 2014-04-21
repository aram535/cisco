package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;

;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:39
 */
public class DefaultPreposConstructor implements PreposConstructor {

    @Autowired
    private SalesService salesService;

    @Override
    public List<Prepos> getPreposes() {

        List<Sale> sales = salesService.getSales(NOT_PROCESSED);
        if (CollectionUtils.isEmpty(sales)) {
            return Lists.newArrayList();
        }

        return null;
    }
}
