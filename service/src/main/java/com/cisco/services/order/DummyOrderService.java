package com.cisco.services.order;


import com.cisco.services.order.dto.Order;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * User: Rost
 * Date: 28.03.14
 * Time: 21:17
 */
@Service("orderService")
public class DummyOrderService implements OrderService {

    @Override
    public Collection<Order> getOrders() {
        return Lists.newArrayList(new Order(1, "first order", 10.0),
                new Order(2, "second order", 20.0), new Order(3, "third order", 30.0));
    }

}
