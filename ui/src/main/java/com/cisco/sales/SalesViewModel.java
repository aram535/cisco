package com.cisco.sales;

import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.cisco.utils.MessageUtils;
import com.google.common.collect.Lists;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
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

	private Sale selectedSaleModel;
	private Sale newSaleModel = new Sale();

	@WireVariable
    private SalesService salesService;

    private List<Sale> notProcessedSales;

	/*public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}*/

	public List<Sale> getNotProcessedSales() {
		try {
			notProcessedSales = salesService.getSales();
			return notProcessedSales;
		} catch (Exception e) {
			MessageUtils.showErrorMessage(e);
			return Lists.newArrayList();
		}
	}

	public Sale getSelectedSaleModel() {
		return selectedSaleModel;
	}

	public Sale getNewSaleModel() {
		return newSaleModel;
	}

	public void setNewSaleModel(Sale newSaleModel) {
		this.newSaleModel = newSaleModel;
	}

	public void setSelectedSaleModel(Sale selectedSaleModel) {
		this.selectedSaleModel = selectedSaleModel;
	}

	@Command("add")
	@NotifyChange("notProcessedSales")
	public void add() {
		newSaleModel.setStatus(Sale.Status.NEW);
		salesService.save(newSaleModel);
		this.newSaleModel = new Sale();
	}

	@Command("update")
	@NotifyChange("notProcessedSales")
	public void update() {
		salesService.update(selectedSaleModel);
	}

	@Command("delete")
	@NotifyChange({"notProcessedSales", "selectedEvent"})
	public void delete() {
		//shouldn't be able to delete with selectedEvent being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedSaleModel != null) {
			salesService.delete(this.selectedSaleModel);
			this.selectedSaleModel = null;
		}
	}
}
