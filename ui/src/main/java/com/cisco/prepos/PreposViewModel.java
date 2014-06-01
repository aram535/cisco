package com.cisco.prepos;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;
import com.cisco.prepos.services.PreposService;
import com.cisco.prepos.services.filter.PreposFilter;
import com.cisco.prepos.services.totalsum.TotalSumCounter;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status.*;

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
	private final List<String> preposStatuses =
			Lists.newArrayList(ALL_STATUS, NOT_PROCESSED.toString(), WAITING.toString(), PROCESSED.toString());

	private String selectedStatus = ALL_STATUS;

	private List<PreposModel> preposes;
    private List<PreposModel> filteredPreposes;
    private Map<Long, PreposModel> checkedPreposMap = Maps.newHashMap();
	private Iterable<PreposModel> filteredCheckedPreposes;

    private PreposRestrictions preposRestrictions = new PreposRestrictions();

    @WireVariable
    private PreposService preposService;

    @WireVariable
    private PreposFilter preposFilter;

    @WireVariable
    private TotalSumCounter totalSumCounter;
	private List<PreposModel> freshPreposes;

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public List<String> getPreposStatuses() {
		return preposStatuses;
	}

	public PreposRestrictions getPreposRestrictions() {
		return preposRestrictions;
	}

	public double getTotalPosSum() {
		if(filteredCheckedPreposes == null) {
			filteredCheckedPreposes = checkedPreposMap.values();
		}
		return totalSumCounter.countTotalPosSum(filteredCheckedPreposes);
	}

	@NotifyChange(RECOUNT_TOTAL_POS_SUM_NOTIFY)
	public List<PreposModel> getAllPrepos() {
		if (preposes == null) {
			refreshAndFilterPreposes();
		}

		return filteredPreposes;
	}

	@NotifyChange(ALL_PREPOS_NOTIFY)
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		refreshAndFilterPreposes();
	}

    @Command(REFRESH_COMMAND)
    @NotifyChange(ALL_PREPOS_NOTIFY)
    public void refresh() {
	    refreshAndFilterPreposes();
    }

    @Command(SAVE_COMMAND)
    public void save() {
        preposService.update(preposes);
    }

    @Command(PROMO_SELECTED_COMMAND)
    public void promoSelected(@BindingParam(PREPOS_MODEL_BINDING_PARAM) PreposModel preposModel, @BindingParam("comboItem") Combobox comboItem) {

	    try {
		    preposService.validatePreposForSelectedDart(preposes, preposModel);
	    } catch(CiscoException ex) {
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
	    notifyChange(preposModel, PREPOS_IN_MODEL_NOTIFY);
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

	private void notifyChange(Object bean, String property) {
		BindUtils.postNotifyChange(null, null, bean, property);
	}

	private void rollbackSelectedItem(PreposModel preposModel, Combobox comboItem) {

		String secondPromo = preposModel.getPrepos().getSecondPromo();

		for (int i = 0; i < comboItem.getItemCount(); i++) {

			Comboitem itemAtIndex = comboItem.getItemAtIndex(i);
			Dart value = itemAtIndex.getValue();
			if(secondPromo.equals(value.getAuthorizationNumber())) {
				comboItem.setSelectedItem(itemAtIndex);
				preposModel.setSelectedDart(value);
				return;
			}
		}
	}

	public void refreshAndFilterPreposes() {

		if (!selectedStatus.equals(ALL_STATUS)) {
			Prepos.Status status = Prepos.Status.valueOf(selectedStatus);
			preposes = preposService.getAllData(status);
		} else {
			preposes = preposService.getAllData();
		}
		filteredPreposes = preposFilter.filter(preposes, preposRestrictions);
	}
}
