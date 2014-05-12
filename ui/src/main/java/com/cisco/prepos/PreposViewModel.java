package com.cisco.prepos;

import com.cisco.prepos.model.PreposFilter;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.PreposService;
import com.google.common.collect.Lists;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class PreposViewModel {

    private List<PreposModel> preposes;
    private List<PreposModel> filteredPreposes;
	private double totalPosSum = 0;
    private PreposFilter preposFilter = new PreposFilter();

    @WireVariable
    private PreposService preposService;

    public List<PreposModel> getAllPrepos() {

        if (preposes == null) {
            preposes = preposService.getAllData();
            filteredPreposes = PreposModel.getFilteredPreposes(preposFilter, Lists.newCopyOnWriteArrayList(preposes));
        }

        return filteredPreposes;
    }

    public PreposFilter getFoodFilter() {
        return preposFilter;
    }

	public double getTotalPosSum() {
		return totalPosSum;
	}

	@Command("refresh")
    @NotifyChange("allPrepos")
    public void refresh() {
        preposes = preposService.getAllData();
	    filteredPreposes = PreposModel.getFilteredPreposes(preposFilter, Lists.newCopyOnWriteArrayList(preposes));
    }

    @Command("save")
    public void save() {
        preposService.update(preposes);
    }


    @Command("promoSelected")
    public void promoSelected(@BindingParam("preposModel") PreposModel preposModel) {

        preposService.recountPrepos(preposModel);
        BindUtils.postNotifyChange(null, null, preposModel, "prepos");
    }

	@Command("preposChecked")
	@NotifyChange({"totalPosSum"})
	public void preposChecked(@BindingParam("preposModel") PreposModel preposModel) {
		if(preposModel.getChecked()) {
			totalPosSum += preposModel.getPrepos().getPosSum();
		} else {
			totalPosSum -= preposModel.getPrepos().getPosSum();
		}
	}

    @Command
    @NotifyChange({"allPrepos"})
    public void changeFilter() {
        filteredPreposes = PreposModel.getFilteredPreposes(preposFilter, preposes);
    }

}
