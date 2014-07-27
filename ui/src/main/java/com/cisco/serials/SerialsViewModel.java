package com.cisco.serials;

import com.cisco.serials.dto.Serial;
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

	private List<Serial> allSerials;

	@WireVariable
	SerialsService serialsService;

	@WireVariable
	SerialsImporter serialsImporter;

	public List<Serial> getAllSerials() {
		try {
			allSerials = serialsService.getAllSerials();
			return allSerials;
		} catch (Exception e) {

			Messagebox.show(ExceptionUtils.getRootCause(e).getMessage(), null, 0, Messagebox.ERROR);
			return Lists.newArrayList();
		}
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
}
