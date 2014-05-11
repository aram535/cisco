package com.cisco.sales.service;

import com.cisco.sales.dao.SalesDao;
import com.cisco.sales.dto.Sale;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
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

	private Table<String, String, Sale> salesTable;

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

	@Override
	public void save(Sale sale) {
		salesDao.save(sale);
	}

	@Override
 	public void update(Sale sale) {
		salesDao.update(sale);
	}

	@Override
	public void update(List<Sale> sales) {
		salesDao.update(sales);
	}

	@Override
	public void delete(Sale sale) {
		salesDao.delete(sale);
	}

	@Override
	public Table<String, String, Sale> getSalesTable() {

		List<Sale> sales = salesDao.getSales();

		return salesListToTable(sales);


	}

	@Override
	public void updateSalesStatuses(List<Sale> sales) {

		for (Sale sale : sales) {
			sale.setStatus(Status.OLD);
		}

		update(sales);
	}

	private Table<String, String, Sale> salesListToTable(List<Sale> sales) {

		HashBasedTable<String, String, Sale> salesTable = HashBasedTable.create();

		for (Sale sale : sales) {
			salesTable.put(sale.getPartNumber(), sale.getShippedBillNumber(), sale);
		}

		return salesTable;
	}

}
