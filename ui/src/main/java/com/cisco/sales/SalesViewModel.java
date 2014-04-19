package com.cisco.sales;

import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * User: Rost
 * Date: 16.04.2014
 * Time: 0:48
 */
@VariableResolver(DelegatingVariableResolver.class)
public class SalesViewModel {


	@WireVariable
    private SalesService salesService;

    private List<Sale> notProcessedSales;

	/*public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}*/

	public List<Sale> getNotProcessedSales() {
        notProcessedSales = salesService.getSales();
        return notProcessedSales;
    }
}
