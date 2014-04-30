package com.cisco.prepos;

import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.PreposService;
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

    @WireVariable
    private PreposService preposService;

    public List<PreposModel> getAllPrepos() {
        preposes = preposService.getAllData();
        return preposes;
    }

    @Command("refresh")
    @NotifyChange("allPrepos")
    public void refresh() {

    }

    @Command("save")
    public void save() {
        preposService.save(preposes);
    }


	@Command("promoSelected")
	public void promoSelected(@BindingParam("preposModel") PreposModel preposModel) {

		preposService.recountPrepos(preposModel);
		BindUtils.postNotifyChange(null, null, preposModel, "prepos");
	}
    public void setPreposService(PreposService preposService) {
        this.preposService = preposService;
    }
}
