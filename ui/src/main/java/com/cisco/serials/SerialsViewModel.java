package com.cisco.serials;

import com.cisco.serials.dto.Serial;
import com.cisco.serials.service.SerialFilter;
import com.cisco.serials.service.SerialsService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

import java.util.List;

/**
 * Created by Alf on 27.07.2014.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class SerialsViewModel {

	public static final String ALL_SERIALS_CHANGE = "allSerials";
	public static final String FILTER_CHANGED_COMMAND = "filterChanged";
	public static final String LOAD_SERIALS_COMMAND = "loadSerials";
	public static final String CLEAR_SERIALS_COMMAND = "clearSerials";

	private String filterSerial = "";

	private List<Serial> allSerials = Lists.newArrayList();
	private List<Serial> filteredSerials = Lists.newArrayList();
	@WireVariable
	SerialsService serialsService;

	@WireVariable
	SerialsImporter serialsImporter;

	@WireVariable
	SerialFilter serialFilter;

	public List<Serial> getAllSerials() {
		try {
			return serialFilter.filter(allSerials, filterSerial);

		} catch (Exception e) {

			Messagebox.show(ExceptionUtils.getRootCause(e).getMessage(), null, 0, Messagebox.ERROR);
			return Lists.newArrayList();
		}
	}

	public String getFilterSerial() {
		return filterSerial;
	}

	public void setFilterSerial(String filterSerial) {
		this.filterSerial = filterSerial;
	}

	@Command
	@NotifyChange({ALL_SERIALS_CHANGE})
	public void importSerials(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
		Media media = event.getMedia();

		try {
		String serialsString = media.getStringData();
		List<Serial> serials = serialsImporter.importSerials(serialsString);
		serialsService.saveOrUpdate(serials);
		} catch (Exception e) {
			Messagebox.show(ExceptionUtils.getRootCause(e).getMessage(), null, 0, Messagebox.ERROR);
		}
	}

	@Command(FILTER_CHANGED_COMMAND)
	@NotifyChange(ALL_SERIALS_CHANGE)
	public void filterChanged() {

	}

	@Command(LOAD_SERIALS_COMMAND)
	@NotifyChange(ALL_SERIALS_CHANGE)
	public void loadSerials() {
		allSerials = serialsService.getAllSerials();
	}

	@Command(CLEAR_SERIALS_COMMAND)
	@NotifyChange(ALL_SERIALS_CHANGE)
	public void clearSerials() {
		allSerials.clear();
	}
}
