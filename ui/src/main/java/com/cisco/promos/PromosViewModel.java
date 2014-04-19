package com.cisco.promos;

import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class PromosViewModel {

	private Promo selectedPromoModel;
	private Promo newPromoModel = new Promo();

	@WireVariable
	private PromosService promosService;

	private List<Promo> promoList;

	public Promo getSelectedPromoModel() {
		return selectedPromoModel;
	}

	public void setSelectedPromoModel(Promo selectedPromoModel) {
		this.selectedPromoModel = selectedPromoModel;
	}

	public Promo getNewPromoModel() {
		return newPromoModel;
	}

	public void setNewPromoModel(Promo newPromoModel) {
		this.newPromoModel = newPromoModel;
	}

	public void setPromoList(List<Promo> promoList) {
		this.promoList = promoList;
	}

	public List<Promo> getAllPromos() {
		return promosService.getPromos();
	}

	@Command("add")
	@NotifyChange("allPromos")
	public void add() {
		promosService.save(newPromoModel);
		this.newPromoModel = new Promo();
	}

	@Command("update")
	@NotifyChange("allPromos")
	public void update() {
		promosService.update(selectedPromoModel);
	}

	@Command("delete")
	@NotifyChange({"allPromos", "selectedEvent"})
	public void delete() {

		if (selectedPromoModel != null) {
			promosService.delete(selectedPromoModel);
			selectedPromoModel = null;
		}
	}
}
