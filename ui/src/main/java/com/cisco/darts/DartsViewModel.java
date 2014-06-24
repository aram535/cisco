package com.cisco.darts;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.excel.DartsImporter;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
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
import org.zkoss.zul.Messagebox;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class DartsViewModel {

	public static final String ALL_DARTS_CHANGE = "allDarts";
	public static final String SELECTED_EVENT_CHANGE = "selectedEvent";

	private Dart selectedDartModel;
    private Dart newDartModel = new Dart();

    @WireVariable
    private DartsService dartsService;

	@WireVariable
	private DartsImporter dartsImporter;

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

	    try {
		    allDarts = dartsService.getDarts();
		    return allDarts;
	    } catch (Exception e) {
		    Messagebox.show(e.getMessage(), null, 0, Messagebox.ERROR);
		    return Lists.newArrayList();
	    }
    }

    @Command("add")
    @NotifyChange(ALL_DARTS_CHANGE)
    public void add() {

        newDartModel.setQuantityInitial(newDartModel.getQuantity());
        dartsService.save(newDartModel);
        this.newDartModel = new Dart();
    }

    @Command("update")
    @NotifyChange(ALL_DARTS_CHANGE)
    public void update() {
        dartsService.update(selectedDartModel);
    }

    @Command("delete")
    @NotifyChange({ALL_DARTS_CHANGE, SELECTED_EVENT_CHANGE})
    public void delete() {
        if (this.selectedDartModel != null) {
            dartsService.delete(this.selectedDartModel);
            this.selectedDartModel = null;
        }
    }

	@Command("deleteAll")
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
}
