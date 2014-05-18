package com.cisco.prepos;

import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;
import com.cisco.prepos.services.PreposService;
import com.cisco.prepos.services.filter.PreposFilter;
import com.google.common.collect.Maps;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 05.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class PreposViewModel {

    private static final String ALL_PREPOS_NOTIFY = "allPrepos";
    private static final String FILTER_CHANGED_COMMAND = "filterChanged";
    private static final String SAVE_COMMAND = "save";
    private static final String REFRESH_COMMAND = "refresh";
    private static final String PROMO_SELECTED_COMMAND = "promoSelected";
    private static final String PREPOS_CHECKED_COMMAND = "preposChecked";
    private static final String RECOUNT_TOTAL_POS_SUM_NOTIFY = "totalPosSum";

    private List<PreposModel> preposes;
    private List<PreposModel> filteredPreposes;
    private Map<Long, PreposModel> checkedPreposes = Maps.newHashMap();

    private PreposRestrictions preposRestrictions = new PreposRestrictions();

    @WireVariable
    private PreposService preposService;

    @WireVariable
    private PreposFilter preposFilter;

    @Command(FILTER_CHANGED_COMMAND)
    public List<PreposModel> getAllPrepos() {
        if (preposes == null) {
            preposes = preposService.getAllData();
        }
        filteredPreposes = preposFilter.filter(preposes, preposRestrictions);
        return filteredPreposes;
    }

    @Command(REFRESH_COMMAND)
    @NotifyChange(ALL_PREPOS_NOTIFY)
    public void refresh() {
        preposes = preposService.getAllData();
        filteredPreposes = preposFilter.filter(preposes, preposRestrictions);
    }

    @Command(SAVE_COMMAND)
    public void save() {
        preposService.update(preposes);
    }

    @Command(PROMO_SELECTED_COMMAND)
    public void promoSelected(@BindingParam("preposModel") PreposModel preposModel) {

        preposService.recountPrepos(preposModel);

        if (preposModel.getChecked()) {
            BindUtils.postNotifyChange(null, null, this, "totalPosSum");
        }

        BindUtils.postNotifyChange(null, null, preposModel, "prepos");
    }

    @Command(PREPOS_CHECKED_COMMAND)
    @NotifyChange({RECOUNT_TOTAL_POS_SUM_NOTIFY})
    public void preposChecked(@BindingParam("preposModel") PreposModel preposModel) {
        if (preposModel.getChecked()) {
            checkedPreposes.put(preposModel.getPrepos().getId(), preposModel);
        } else {
            checkedPreposes.remove(preposModel.getPrepos().getId());
        }
    }

    public PreposRestrictions getPreposRestrictions() {
        return preposRestrictions;
    }


    public double getTotalPosSum() {
        return countTotalPosSum();
    }

    //TODO move to separate entity TotalPosSumCounter
    private double countTotalPosSum() {

        double totalPosSum = 0;
        for (PreposModel preposModel : checkedPreposes.values()) {
            totalPosSum += preposModel.getPrepos().getPosSum();
        }
        totalPosSum = (double) Math.round(totalPosSum * 100) / 100;
        return totalPosSum;
    }

}
