package com.cisco.services.order;

import com.cisco.services.order.dto.Order;

import java.util.Collection;

/**
 * User: Rost
 * Date: 28.03.14
 * Time: 21:03
 */
public interface OrderService {
    Collection<Order> getOrders();
}
