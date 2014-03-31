package com.cisco.services.order;

import com.cisco.services.order.dto.Order;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import java.util.Collection;

/**
 * User: Rost
 * Date: 28.03.14
 * Time: 22:04
 */
@VariableResolver(DelegatingVariableResolver.class)
public class OrdersController extends SelectorComposer<Component> {


    @Wire
    private Listbox ordersListBox;

    @WireVariable
    private OrderService orderService;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Collection<Order> orders = orderService.getOrders();
        ListModel<Order> ordersListModel = new ListModelList<Order>(orders);
        ordersListBox.setModel(ordersListModel);
    }
}
