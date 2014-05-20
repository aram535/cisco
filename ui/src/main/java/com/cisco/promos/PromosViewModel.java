package com.cisco.promos;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.excel.PromosImporter;
import com.cisco.promos.service.PromosService;
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

    private static final String ALL_PROMOS_CHANGE = "allPromos";
    private static final String SELECTED_EVENT_CHANGE = "selectedEvent";

    private Promo selectedPromoModel;
    private Promo newPromoModel = new Promo();

    @WireVariable
    private PromosService promosService;

    @WireVariable
    private PromosImporter promosImporter;

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
    @NotifyChange(ALL_PROMOS_CHANGE)
    public void add() {
        promosService.save(newPromoModel);
        this.newPromoModel = new Promo();
    }

    @Command("update")
    @NotifyChange(ALL_PROMOS_CHANGE)
    public void update() {
        promosService.update(selectedPromoModel);
    }

    @Command("delete")
    @NotifyChange({ALL_PROMOS_CHANGE, SELECTED_EVENT_CHANGE})
    public void delete() {

        if (selectedPromoModel != null) {
            promosService.delete(selectedPromoModel);
            selectedPromoModel = null;
        }
    }

	@Command("delete")
	@NotifyChange({ALL_PROMOS_CHANGE, SELECTED_EVENT_CHANGE})
	public void deleteAll() {

			promosService.deleteAll();
			selectedPromoModel = null;
	}

    @Command
    @NotifyChange({ALL_PROMOS_CHANGE})
    public void importPromos(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
        Media media = event.getMedia();
        if (media.isBinary()) {
            InputStream inputStream = media.getStreamData();
            promosImporter.importPromos(inputStream);
        } else {
            throw new CiscoException("media is not binary");
        }
    }

    void setPromosImporter(PromosImporter promosImporter) {
        this.promosImporter = promosImporter;
    }
}
