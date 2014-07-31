package com.cisco.sales.service;

import com.cisco.sales.dao.SalesDao;
import com.cisco.sales.dto.Sale;
import com.google.common.base.Predicate;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.cisco.sales.dto.Sale.Status;
import static com.cisco.sales.dto.Sale.Status.OLD;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.HashBasedTable.create;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * User: Rost
 * Date: 15.04.2014
 * Time: 23:59
 */
@Service("salesService")
public class DefaultSalesService implements SalesService {

    @Autowired
    private SalesDao salesDao;

    @Cacheable(value = "salesCache")
    @Transactional
    @Override
    public List<Sale> getSales(final Status... statuses) {

        List sales = salesDao.getSales();

        if (isEmpty(statuses)) {
            return newArrayList(sales);
        }

        Collection<Sale> filteredSales = filter(sales, new Predicate<Sale>() {
            @Override
            public boolean apply(Sale sale) {
                Status status = sale.getStatus();
                return contains(statuses, status);
            }
        });
        return newArrayList(filteredSales);
    }

    @CacheEvict(value = "salesCache")
    @Transactional
    @Override
    public void save(Sale sale) {
        salesDao.save(sale);
    }

    @CacheEvict(value = "salesCache")
    @Transactional
    @Override
    public void update(Sale sale) {
        salesDao.update(sale);
    }

    @CacheEvict(value = "salesCache")
    @Transactional
    @Override
    public void update(List<Sale> sales) {
        salesDao.update(sales);
    }

    @CacheEvict(value = "salesCache")
    @Transactional
    @Override
    public void delete(Sale sale) {
        salesDao.delete(sale);
    }

    @Transactional
    @Override
    public Table<String, String, Sale> getSalesTable() {

        List<Sale> sales = salesDao.getSales();

        return salesListToTable(sales);
    }

    @Override
    public void updateSalesStatuses(List<Sale> sales) {

        for (Sale sale : sales) {
            sale.setStatus(OLD);
        }

        update(sales);
    }

    private Table<String, String, Sale> salesListToTable(List<Sale> sales) {

        Table<String, String, Sale> salesTable = create();

        for (Sale sale : sales) {
            salesTable.put(sale.getPartNumber(), sale.getShippedBillNumber(), sale);
        }

        return salesTable;
    }

}
