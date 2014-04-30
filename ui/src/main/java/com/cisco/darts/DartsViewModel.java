package com.cisco.darts;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class DartsViewModel {

	private Dart selectedDartModel;
	private Dart newDartModel = new Dart();

	@WireVariable
	private DartsService dartsService;

	private List<Dart> allDarts;

	public Dart getSelectedDartModel() {
		return selectedDartModel;
	}

	public Dart getNewDartModel() {
		return newDartModel;
	}

	public void setSelectedDartModel(Dart selectedDartModel) {
		this.selectedDartModel = selectedDartModel;
	}

	public void setNewDartModel(Dart newDartModel) {
		this.newDartModel = newDartModel;
	}

	public void setDartsService(DartsService DartsService) {
		this.dartsService = DartsService;
	}

	public List<Dart> getAllDarts() {
		allDarts = dartsService.getDarts();
		return allDarts;
	}

	@Command("add")
	@NotifyChange("allDarts")
	public void add() {

		newDartModel.setQuantityInitial(newDartModel.getQuantity());
		dartsService.save(newDartModel);
		this.newDartModel = new Dart();

	}

	@Command("update")
	@NotifyChange("allDarts")
	public void update() {
		dartsService.update(selectedDartModel);
	}

	@Command("delete")
	@NotifyChange({"allDarts", "selectedEvent"})
	public void delete() {
		//shouldn't be able to delete with selectedEvent being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedDartModel != null) {
			dartsService.delete(this.selectedDartModel);
			this.selectedDartModel = null;
		}
	}


}
