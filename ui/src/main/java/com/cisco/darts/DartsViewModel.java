package com.cisco.darts;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.excel.DartsImporter;
import com.cisco.darts.service.DartsFilter;
import com.cisco.darts.service.DartsRestrictions;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.utils.MessageUtils;
import com.google.common.collect.Lists;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class DartsViewModel {

	public static final String ALL_DARTS_CHANGE = "allDarts";
	public static final String SELECTED_EVENT_CHANGE = "selectedEvent";
	public static final String DELETE_ALL_COMMAND = "deleteAll";
	public static final String DELETE_COMMAND = "delete";
	public static final String UPDATE_COMMAND = "update";
	public static final String ADD_COMMAND = "add";
	public static final String FILTER_CHANGED_COMMAND = "filterChanged";

	private Dart selectedDartModel;
    private Dart newDartModel = new Dart();

    @WireVariable
    private DartsService dartsService;

	@WireVariable
	private DartsImporter dartsImporter;

	@WireVariable
	private DartsRestrictions dartsRestrictions;

	@WireVariable
	private DartsFilter dartsFilter;

    private List<Dart> allDarts;
    private List<Dart> filteredDarts = Lists.newArrayList();

    public Dart getSelectedDartModel() {
        return selectedDartModel;
    }

    public Dart getNewDartModel() {
        return newDartModel;
    }

	public DartsRestrictions getDartsRestrictions() {
		return dartsRestrictions;
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

	public void setDartsRestrictions(DartsRestrictions dartsRestrictions) {
		this.dartsRestrictions = dartsRestrictions;
	}

	public List<Dart> getAllDarts() {

		try {
			allDarts = dartsService.getDarts();
			filteredDarts = dartsFilter.filter(allDarts, dartsRestrictions);
			return filteredDarts;
		} catch (Exception e) {
			MessageUtils.showErrorMessage(e);
			return Lists.newArrayList();
		}
    }

    @Command(ADD_COMMAND)
    @NotifyChange(ALL_DARTS_CHANGE)
    public void add() {

        newDartModel.setQuantityInitial(newDartModel.getQuantity());
        dartsService.save(newDartModel);
        this.newDartModel = new Dart();
    }

    @Command(UPDATE_COMMAND)
    @NotifyChange(ALL_DARTS_CHANGE)
    public void update() {
        dartsService.update(selectedDartModel);
    }

    @Command(DELETE_COMMAND)
    @NotifyChange({ALL_DARTS_CHANGE, SELECTED_EVENT_CHANGE})
    public void delete() {
        if (this.selectedDartModel != null) {
            dartsService.delete(this.selectedDartModel);
            this.selectedDartModel = null;
        }
    }

	@Command(DELETE_ALL_COMMAND)
	@NotifyChange({ALL_DARTS_CHANGE, SELECTED_EVENT_CHANGE})
	public void deleteAll() {

		dartsService.deleteAll();
		this.selectedDartModel = null;
	}

	@Command
	@NotifyChange({ALL_DARTS_CHANGE})
	public void importDarts(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
		Media media = event.getMedia();
		if (media.isBinary()) {
			InputStream inputStream = media.getStreamData();
			dartsImporter.importDarts(inputStream);
		} else {
			throw new CiscoException("media is not binary");
		}
	}

	@Command(FILTER_CHANGED_COMMAND)
	@NotifyChange(ALL_DARTS_CHANGE)
	public void filterChanged() {
		filteredDarts = dartsFilter.filter(allDarts, dartsRestrictions);
	}
}
