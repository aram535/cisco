package com.cisco.sales.dao;

import com.cisco.sales.dto.Sale;

import java.util.List;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:26
 */
public interface SalesDao {
    List<Sale> getSales();

	void save(Sale sale);

	void update(Sale sale);

	void update(List<Sale> sales);

	void delete(Sale sale);

}
