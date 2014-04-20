package com.cisco.pricelists;

import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * Created by Alf on 20.04.2014.
 */
@VariableResolver(DelegatingVariableResolver.class)
public class PricelistsViewModel {

	private Pricelist selectedPricelistModel;
	private Pricelist newPricelistModel = new Pricelist();

	@WireVariable
	private PricelistsService pricelistsService;

	private List<Pricelist> allPricelists;

	public Pricelist getSelectedPricelistModel() {
		return selectedPricelistModel;
	}

	public Pricelist getNewPricelistModel() {
		return newPricelistModel;
	}

	public void setSelectedPricelistModel(Pricelist selectedPricelistModel) {
		this.selectedPricelistModel = selectedPricelistModel;
	}

	public void setNewPricelistModel(Pricelist newPricelistModel) {
		this.newPricelistModel = newPricelistModel;
	}

	public void setPricelistsService(PricelistsService pricelistsService) {
		this.pricelistsService = pricelistsService;
	}

	public List<Pricelist> getAllPricelists() {
		allPricelists = pricelistsService.getPricelists();
		return allPricelists;
	}

	@Command("add")
	@NotifyChange("allPricelists")
	public void add() {

		pricelistsService.save(newPricelistModel);
		this.newPricelistModel = new Pricelist();
	}

	@Command("update")
	@NotifyChange("allPricelists")
	public void update() {
		pricelistsService.update(selectedPricelistModel);
	}

	@Command("delete")
	@NotifyChange({"allPricelists", "selectedEvent"})
	public void delete() {
		//shouldn't be able to delete with selectedEvent being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedPricelistModel != null) {
			pricelistsService.delete(this.selectedPricelistModel);
			this.selectedPricelistModel = null;
		}
	}
}
