package com.cisco.sales.service;

import com.cisco.sales.dao.SalesDao;
import com.cisco.sales.dto.Sale;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.cisco.sales.dto.Sale.Status;

/**
 * User: Rost
 * Date: 15.04.2014
 * Time: 23:59
 */
@Service("salesService")
public class DefaultSalesService implements SalesService {

    @Autowired
    private SalesDao salesDao;

    @Override
    public List<Sale> getSales(final Status... statuses) {

        List sales = salesDao.getSales();

        if (ArrayUtils.isEmpty(statuses)) {
            return Lists.newArrayList(sales);
        }

        Collection<Sale> filteredSales = Collections2.filter(sales, new Predicate<Sale>() {
            @Override
            public boolean apply(Sale sale) {
                Status status = sale.getStatus();
                return ArrayUtils.contains(statuses, status);
            }
        });
        return Lists.newArrayList(filteredSales);
    }
}
