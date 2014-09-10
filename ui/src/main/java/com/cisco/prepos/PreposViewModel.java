package com.cisco.prepos;

import com.cisco.claims.ClaimsImporter;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;
import com.cisco.prepos.services.PreposService;
import com.cisco.prepos.services.filter.PreposFilter;
import com.cisco.prepos.services.totalsum.TotalSumCounter;
import com.cisco.utils.MessageUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cisco.prepos.dto.Prepos.Status.*;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Alf on 05.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class PreposViewModel {

	private static final String ALL_PREPOS_NOTIFY = "allPrepos";
    private static final String FILTER_CHANGED_COMMAND = "filterChanged";
    private static final String STATUS_FILTER_CHANGED_COMMAND = "statusFilterChanged";
    private static final String SAVE_COMMAND = "save";
    private static final String REFRESH_COMMAND = "refresh";
    private static final String PROMO_SELECTED_COMMAND = "promoSelected";
    private static final String PREPOS_CHECKED_COMMAND = "preposChecked";
    private static final String RECOUNT_TOTAL_POS_SUM_NOTIFY = "totalPosSum";
    private static final String PREPOS_IN_MODEL_NOTIFY = "prepos";
    private static final String PREPOS_MODEL_BINDING_PARAM = "preposModel";
    public static final String ALL_STATUS = "ALL";
    public static final String SET_STATUS_COMMAND = "setStatus";
	public static final String EXPORT_POSREADY_COMMAND = "exportPosready";
	public static final String CHECK_ALL_COMMAND = "checkAll";
	public static final String IMPORT_CLAIMS_COMMAND = "importClaims";
	public static final String ACTION_MESSAGE = "actionMessage";

	private final List<String> preposStatusesToChange =
			newArrayList(CBN.getName(), CANCEL.getName());

	private final List<String> preposStatuses =
            newArrayList(ALL_STATUS, NOT_POS.getName(), WAIT.getName(), POS_OK.getName(),
                    CBN.getName(), CANCEL.getName());

    private String selectedStatus = NOT_POS.getName();
    private String statusToChange = CBN.toString();
	private boolean checkAll = false;

    private List<PreposModel> preposes;
    private List<PreposModel> filteredPreposes = Lists.newArrayList();
    private Map<Long, PreposModel> checkedPreposMap = Maps.newHashMap();
    private Iterable<PreposModel> filteredCheckedPreposes;

	@WireVariable
    private PreposRestrictions preposRestrictions;

    @WireVariable
    private PreposService preposService;

    @WireVariable
    private PreposFilter preposFilter;

	@WireVariable
	private ClaimsImporter claimsImporter;

    @WireVariable
    private TotalSumCounter totalSumCounter;
    private List<PreposModel> freshPreposes;

	public boolean getCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}

	public String getStatusToChange() {
        return statusToChange;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public List<String> getPreposStatuses() {
        return preposStatuses;
    }

	public List<String> getPreposStatusesToChange() {
		return preposStatusesToChange;
	}

	public PreposRestrictions getPreposRestrictions() {
        return preposRestrictions;
    }

    public double getTotalPosSum() {
        if (filteredCheckedPreposes == null) {
            filteredCheckedPreposes = checkedPreposMap.values();
        }
        return totalSumCounter.countTotalPosSum(filteredCheckedPreposes);
    }

    public void setStatusToChange(String statusToChange) {
        this.statusToChange = statusToChange;
    }

    @NotifyChange(RECOUNT_TOTAL_POS_SUM_NOTIFY)
    public List<PreposModel> getAllPrepos() {
        try {
            if (CollectionUtils.isEmpty(preposes)) {
                refreshAndFilterPreposes();
            }

            return filteredPreposes;
        } catch (Exception e) {
	        MessageUtils.showErrorMessage(e);
            return newArrayList();
        }
    }

	@NotifyChange(ALL_PREPOS_NOTIFY)
    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
	    try {
		    refreshAndFilterPreposes();
	    } catch (Exception e) {
		    MessageUtils.showErrorMessage(e);
	    }
    }

    @Command(SET_STATUS_COMMAND)
    @NotifyChange({ALL_PREPOS_NOTIFY, "actionMessage"})
    public void setStatus() {

	    if(actionMessage == null) {
		    actionMessage = "Are you sure?";
		    currentCommand = SET_STATUS_COMMAND;
	    } else {
		    clearActionMessageAndCommand();
		    for (PreposModel checkedPrepos : checkedPreposMap.values()) {
			    Prepos.Status currentStatus = checkedPrepos.getPrepos().getStatus();
			    if(!currentStatus.equals(CBN) && !currentStatus.equals(CANCEL)) {
				    checkedPrepos.getPrepos().setStatus(Prepos.Status.valueOf(statusToChange));
			    }
		    }
	    }
    }

    @Command(REFRESH_COMMAND)
    @NotifyChange(ALL_PREPOS_NOTIFY)
    public void refresh() {
        refreshAndFilterPreposes();
    }

    @Command(SAVE_COMMAND)
    @NotifyChange({ALL_PREPOS_NOTIFY, ACTION_MESSAGE})
    public void save() {
	    if(actionMessage == null) {
		    actionMessage = "Are you sure?";
		    currentCommand = SAVE_COMMAND;
	    } else {
		    clearActionMessageAndCommand();
		    preposService.updateFromModels(preposes);
	    }

    }

    @Command(PROMO_SELECTED_COMMAND)
    public void promoSelected(@BindingParam(PREPOS_MODEL_BINDING_PARAM) PreposModel preposModel, @BindingParam("comboItem") Combobox comboItem) {

        try {
            preposService.validatePreposForSelectedDart(preposes, preposModel);
        } catch (CiscoException ex) {
            rollbackSelectedItem(preposModel, comboItem);
            throw ex;
        }

        Prepos prepos = preposModel.getPrepos();
        Dart selectedDart = preposModel.getSelectedDart();
        Prepos recountedPrepos = preposService.recountPrepos(prepos, selectedDart);
        preposModel.setPrepos(recountedPrepos);

        if (preposModel.getChecked()) {
            notifyChange(this, RECOUNT_TOTAL_POS_SUM_NOTIFY);
        }
        notifyChange(preposModel, PREPOS_IN_MODEL_NOTIFY, "buyDiscount", "saleDiscount");
    }

    @Command(PREPOS_CHECKED_COMMAND)
    @NotifyChange(RECOUNT_TOTAL_POS_SUM_NOTIFY)
    public void preposChecked(@BindingParam(PREPOS_MODEL_BINDING_PARAM) PreposModel preposModel) {
        if (preposModel.getChecked()) {
            checkedPreposMap.put(preposModel.getPrepos().getId(), preposModel);
        } else {
            checkedPreposMap.remove(preposModel.getPrepos().getId());
        }
    }

    @Command(FILTER_CHANGED_COMMAND)
    @NotifyChange(ALL_PREPOS_NOTIFY)
    public void filterChanged() {
        filteredPreposes = preposFilter.filter(preposes, preposRestrictions);

        final Collection<PreposModel> checkedPreposes = checkedPreposMap.values();
        filteredCheckedPreposes = Iterables.filter(filteredPreposes, new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                return checkedPreposes.contains(preposModel);
            }
        });
    }

    @Command(STATUS_FILTER_CHANGED_COMMAND)
    @NotifyChange(ALL_PREPOS_NOTIFY)
    public void statusFilterChanged() {
        filteredPreposes = preposFilter.filter(preposes, preposRestrictions);

        final Collection<PreposModel> checkedPreposes = checkedPreposMap.values();
        filteredCheckedPreposes = Iterables.filter(filteredPreposes, new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                return checkedPreposes.contains(preposModel);
            }
        });
    }

	@Command(EXPORT_POSREADY_COMMAND)
	@NotifyChange({ALL_PREPOS_NOTIFY, ACTION_MESSAGE})
	public void exportPosready() {

		if(actionMessage == null) {
			actionMessage = "Are you sure?";
			currentCommand = EXPORT_POSREADY_COMMAND;
		} else {
			clearActionMessageAndCommand();
			Collection<PreposModel> preposes = checkedPreposMap.values();

			if (!preposes.isEmpty()) {

				String filePath = preposService.exportPosready(preposes);

				try {
					InputStream is = new FileInputStream(filePath);
					Filedownload.save(is, "xls", Paths.get(filePath).getFileName().toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void clearActionMessageAndCommand() {
		actionMessage = null;
		currentCommand = null;
	}

	@Command(IMPORT_CLAIMS_COMMAND)
	@NotifyChange(ALL_PREPOS_NOTIFY)
	public void importClaims(@ContextParam(ContextType.TRIGGER_EVENT) final UploadEvent event) {

		Media media = event.getMedia();
		if (media.isBinary()) {
			InputStream inputStream = media.getStreamData();
			claimsImporter.importClaims(inputStream);
		} else {
			throw new CiscoException("media is not binary");
		}

		Messagebox.show("Claims were successfuly imported", null, 0, Messagebox.INFORMATION);
		refresh();
	}

	@Command(CHECK_ALL_COMMAND)
	@NotifyChange({RECOUNT_TOTAL_POS_SUM_NOTIFY, ALL_PREPOS_NOTIFY})
	public void checkAll() {
		if(checkAll) {
			Map<Long, PreposModel> allCheckPreposes = Maps.uniqueIndex(filteredPreposes, new Function<PreposModel, Long>() {
				@Override
				public Long apply(PreposModel model) {
					model.setChecked(true);
					return model.getPrepos().getId();
				}
			});

			checkedPreposMap.putAll(allCheckPreposes);
		} else {
			for (PreposModel preposModel : filteredPreposes) {
				preposModel.setChecked(false);
			}
			checkedPreposMap.clear();
		}
	}

    private void notifyChange(Object bean, String... properties) {

        for (String property : properties) {
            BindUtils.postNotifyChange(null, null, bean, property);
        }

    }

    private void rollbackSelectedItem(PreposModel preposModel, Combobox comboItem) {

        String secondPromo = preposModel.getPrepos().getSecondPromo();

        for (int i = 0; i < comboItem.getItemCount(); i++) {

            Comboitem itemAtIndex = comboItem.getItemAtIndex(i);
            Dart value = itemAtIndex.getValue();
            if (secondPromo.equals(value.getAuthorizationNumber())) {
		        comboItem.setSelectedItem(itemAtIndex);
		        preposModel.setSelectedDart(value);
		        return;
	        }
        }
    }

    private void refreshAndFilterPreposes() {
	    Set<Long> checkedPreposesIds = checkedPreposMap.keySet();

	    if (!selectedStatus.equals(ALL_STATUS)) {
            Prepos.Status status = Prepos.Status.valueOf(selectedStatus);
            preposes = preposService.getAllData(status);
        } else {
            preposes = preposService.getAllData();
        }
        filteredPreposes = preposFilter.filter(preposes, preposRestrictions);
	    checkPreposes(checkedPreposesIds);
    }

	@NotifyChange()
	private void checkPreposes(Set<Long> checkedPreposesIds) {

	}

	//Confirmation stuff
	private String actionMessage;
	private String currentCommand;

	public String getCurrentCommand() {
		return currentCommand;
	}

	public void setCurrentCommand(String currentCommand) {
		this.currentCommand = currentCommand;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	@Command @NotifyChange("actionMessage")
	public void confirmAction(){
		//set the message to show to user
		actionMessage = "Are you sure?";
	}

	@Command @NotifyChange("actionMessage")
	public void cancelAction(){
		//clear the message
		clearActionMessageAndCommand();
	}
}
