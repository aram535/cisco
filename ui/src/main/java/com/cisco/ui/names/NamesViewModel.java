package com.cisco.ui.names;

import com.cisco.names.dto.Names;
import com.cisco.names.service.NamesService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class NamesViewModel {

	private Names selectedNamesModel;
	private Names newNamesModel = new Names();

	@WireVariable
	private NamesService namesService;
	private List<Names> allNames;

	public Names getSelectedNamesModel() {
		return selectedNamesModel;
	}

	public Names getNewNamesModel() {
		return newNamesModel;
	}

	public void setSelectedNamesModel(Names selectedNamesModel) {
		this.selectedNamesModel = selectedNamesModel;
	}

	public void setNewNamesModel(Names newNamesModel) {
		this.newNamesModel = newNamesModel;
	}

	public void setNamesService(NamesService namesService) {
		this.namesService = namesService;
	}

	public List<Names> getAllNames() {
		allNames = namesService.getAllData();
		return allNames;
	}

	@Command("add")
	@NotifyChange("allNames")
	public void add() {
		newNamesModel.setId(UUID.randomUUID().toString());
		namesService.save(newNamesModel);
		this.newNamesModel = new Names();
	}

	@Command("update")
	@NotifyChange("allNames")
	public void update() {
		namesService.update(selectedNamesModel);
	}

	@Command("delete")
	@NotifyChange({"allNames", "selectedEvent"})
	public void delete() {
		//shouldn't be able to delete with selectedEvent being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedNamesModel != null) {
			namesService.delete(this.selectedNamesModel);
			this.selectedNamesModel = null;
		}
	}
}
