package com.cisco.pricelists;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.excel.PricelistImporter;
import com.cisco.pricelists.service.PricelistsService;
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
 * Created by Alf on 20.04.2014.
 */
@VariableResolver(DelegatingVariableResolver.class)
public class PricelistsViewModel {

    private static final String ALL_PRICELISTS_CHANGE = "allPricelists";
    private static final String SELECTED_EVENT_CHANGE = "selectedEvent";

    private Pricelist selectedPricelistModel;
    private Pricelist newPricelistModel = new Pricelist();

    @WireVariable
    private PricelistsService pricelistsService;

    @WireVariable
    private PricelistImporter pricelistImporter;

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
    @NotifyChange(ALL_PRICELISTS_CHANGE)
    public void add() {

        pricelistsService.save(newPricelistModel);
        this.newPricelistModel = new Pricelist();
    }

    @Command("update")
    @NotifyChange(ALL_PRICELISTS_CHANGE)
    public void update() {
        pricelistsService.update(selectedPricelistModel);
    }

    @Command("delete")
    @NotifyChange({ALL_PRICELISTS_CHANGE, SELECTED_EVENT_CHANGE})
    public void delete() {
        //shouldn't be able to delete with selectedEvent being null anyway
        //unless trying to hack the system, so just ignore the request
        if (this.selectedPricelistModel != null) {
            pricelistsService.delete(this.selectedPricelistModel);
            this.selectedPricelistModel = null;
        }
    }

    @Command
    @NotifyChange({ALL_PRICELISTS_CHANGE})
    public void importPricelist(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
        Media media = event.getMedia();
        if (media.isBinary()) {
            InputStream inputStream = media.getStreamData();
            pricelistImporter.importPricelist(inputStream);
        } else {
            throw new CiscoException("media is not binary");
        }
    }

    void setPricelistImporter(PricelistImporter pricelistImporter) {
        this.pricelistImporter = pricelistImporter;
    }
}
