package com.cisco.sales.service;

import com.cisco.sales.dto.Sale;

import java.util.List;

import static com.cisco.sales.dto.Sale.Status;

/**
 * User: Rost
 * Date: 15.04.2014
 * Time: 23:53
 */
public interface SalesService {
    List<Sale> getSales(Status... statuses);
}
