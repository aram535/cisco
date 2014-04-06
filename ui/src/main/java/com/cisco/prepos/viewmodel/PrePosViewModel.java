package com.cisco.prepos.viewmodel;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.PreposService;
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

    private List<Prepos> preposes;

    @WireVariable
    private PreposService preposService;

    public List<Prepos> getAllPrepos() {
        preposes = preposService.getAllData();
        return preposes;
    }

    @Command("add")
    @NotifyChange("events")
    public void add() {

    }

    @Command("update")
    public void update() {
        preposService.update(preposes);
    }

}
