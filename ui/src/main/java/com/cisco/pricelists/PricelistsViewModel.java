package com.cisco.pricelists;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.excel.PricelistImporter;
import com.cisco.pricelists.service.PricelistFilter;
import com.cisco.pricelists.service.PricelistRestrictions;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.utils.MessageUtils;
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

import java.io.InputStream;
import java.util.List;

/**
 * Created by Alf on 20.04.2014.
 */
@VariableResolver(DelegatingVariableResolver.class)
public class PricelistsViewModel {

	private static final String FILTER_CHANGED_COMMAND = "filterChanged";
    private static final String ALL_PRICELISTS_NOTIFY = "allPricelists";
    private static final String SELECTED_EVENT_CHANGE = "selectedEvent";

    private Pricelist selectedPricelistModel;
    private Pricelist newPricelistModel = new Pricelist();

    @WireVariable
    private PricelistsService pricelistsService;

    @WireVariable
    private PricelistImporter pricelistImporter;

	private PricelistRestrictions pricelistRestrictions = new PricelistRestrictions();

	@WireVariable
	private PricelistFilter pricelistFilter;

    private List<Pricelist> allPricelists;
    private List<Pricelist> filteredPricelists;

	public PricelistRestrictions getPricelistRestrictions() {
		return pricelistRestrictions;
	}

	public void setPricelistRestrictions(PricelistRestrictions pricelistRestrictions) {
		this.pricelistRestrictions = pricelistRestrictions;
	}

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
        try {
            allPricelists = pricelistsService.getPricelists();
	        filteredPricelists = pricelistFilter.filter(allPricelists, pricelistRestrictions);
            return filteredPricelists;
        } catch (Exception e) {
	        MessageUtils.showErrorMessage(e);
            return Lists.newArrayList();
        }
    }

    @Command("add")
    @NotifyChange(ALL_PRICELISTS_NOTIFY)
    public void add() {

        try {
            pricelistsService.save(newPricelistModel);
            this.newPricelistModel = new Pricelist();
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }

    }

    @Command("update")
    @NotifyChange(ALL_PRICELISTS_NOTIFY)
    public void update() {
        try {
            pricelistsService.update(selectedPricelistModel);
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    @Command("delete")
    @NotifyChange({ALL_PRICELISTS_NOTIFY, SELECTED_EVENT_CHANGE})
    public void delete() {
        try {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if (this.selectedPricelistModel != null) {
                pricelistsService.delete(this.selectedPricelistModel);
                this.selectedPricelistModel = null;
            }
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }

    }

    @Command("deleteAll")
    @NotifyChange({ALL_PRICELISTS_NOTIFY, SELECTED_EVENT_CHANGE})
    public void deleteAll() {

        try {
            pricelistsService.deleteAll();
            this.selectedPricelistModel = null;
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }

    }

    @Command
    @NotifyChange({ALL_PRICELISTS_NOTIFY})
    public void importPricelist(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {

        Media media = event.getMedia();
        if (media.isBinary()) {
            InputStream inputStream = media.getStreamData();
            pricelistImporter.importPricelist(inputStream);
        } else {
            throw new CiscoException("media is not binary");
        }
    }

	@Command(FILTER_CHANGED_COMMAND)
	@NotifyChange(ALL_PRICELISTS_NOTIFY)
	public void filterChanged() {

	}

    void setPricelistImporter(PricelistImporter pricelistImporter) {
        this.pricelistImporter = pricelistImporter;
    }
}
