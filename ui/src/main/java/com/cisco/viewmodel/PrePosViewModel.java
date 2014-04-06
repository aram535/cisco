package com.cisco.viewmodel;

import com.cisco.model.PrePosModel;
import com.cisco.services.CiscoController;
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
public class PrePosViewModel {

    private List<PrePosModel> prePosModelList;

    @WireVariable
    private CiscoController ciscoController;

    public List<PrePosModel> getAllPrePos() {
        prePosModelList = ciscoController.getAllPrePosData();
        return prePosModelList;
    }

    @Command("add")
    @NotifyChange("events")
    public void add() {

    }

    @Command("update")
    public void update() {
        ciscoController.updatePrePos(prePosModelList);
    }

}
