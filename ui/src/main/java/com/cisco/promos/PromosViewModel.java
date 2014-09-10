package com.cisco.promos;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.excel.PromosImporter;
import com.cisco.promos.service.PromosFilter;
import com.cisco.promos.service.PromosRestrictions;
import com.cisco.promos.service.PromosService;
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
 * Created by Alf on 19.04.2014.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class PromosViewModel {

	private static final String FILTER_CHANGED_COMMAND = "filterChanged";
    private static final String ALL_PROMOS_NOTIFY = "allPromos";
    private static final String SELECTED_EVENT_CHANGE = "selectedEvent";

    private Promo selectedPromoModel;
    private Promo newPromoModel = new Promo();

    @WireVariable
    private PromosService promosService;

    @WireVariable
    private PromosImporter promosImporter;

	private PromosRestrictions promosRestrictions = new PromosRestrictions();

	@WireVariable
	private PromosFilter promosFilter;

    private List<Promo> promoList;
	private List<Promo> filteredPromos;

	public PromosRestrictions getPromosRestrictions() {
		return promosRestrictions;
	}

	public void setPromosRestrictions(PromosRestrictions promosRestrictions) {
		this.promosRestrictions = promosRestrictions;
	}

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
	    try {
		    promoList = promosService.getPromos();
		    filteredPromos = promosFilter.filter(promoList, promosRestrictions);
		    return filteredPromos;
	    } catch (Exception e) {
		    MessageUtils.showErrorMessage(e);
		    return Lists.newArrayList();
	    }
    }

    @Command("add")
    @NotifyChange(ALL_PROMOS_NOTIFY)
    public void add() {
        promosService.save(newPromoModel);
        this.newPromoModel = new Promo();
    }

    @Command("update")
    @NotifyChange(ALL_PROMOS_NOTIFY)
    public void update() {
        promosService.update(selectedPromoModel);
    }

    @Command("delete")
    @NotifyChange({ALL_PROMOS_NOTIFY, SELECTED_EVENT_CHANGE})
    public void delete() {

        if (selectedPromoModel != null) {
            promosService.delete(selectedPromoModel);
            selectedPromoModel = null;
        }
    }

	@Command("deleteAll")
	@NotifyChange({ALL_PROMOS_NOTIFY, SELECTED_EVENT_CHANGE})
	public void deleteAll() {

		try {
			promosService.deleteAll();
			selectedPromoModel = null;
		} catch (Exception e) {
			MessageUtils.showErrorMessage(e);
		}
	}

    @Command
    @NotifyChange({ALL_PROMOS_NOTIFY})
    public void importPromos(@ContextParam(ContextType.TRIGGER_EVENT) final UploadEvent event) {

	    Media media = event.getMedia();
	    if (media.isBinary()) {
		    InputStream inputStream = media.getStreamData();
		    promosImporter.importPromos(inputStream);
	    } else {
		    throw new CiscoException("media is not binary");
	    }
    }

	@Command(FILTER_CHANGED_COMMAND)
	@NotifyChange(ALL_PROMOS_NOTIFY)
	public void filterChanged() {

	}

    void setPromosImporter(PromosImporter promosImporter) {
        this.promosImporter = promosImporter;
    }
}
