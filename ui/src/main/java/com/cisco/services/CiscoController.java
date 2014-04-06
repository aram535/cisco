package com.cisco.services;

import com.cisco.dto.PrePos;
import com.cisco.model.PrePosModel;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */

@Service
public class CiscoController {

    @Autowired
    private PrePosService prePosService;

    public List<PrePosModel> getAllPrePosData() {
        List<PrePos> prePosList = prePosService.getAllData();

        List<PrePosModel> resultModelList = new ArrayList<>();
        for (PrePos prePos : prePosList) {
            resultModelList.add(
                    new PrePosModel(prePos.getTYPE(), prePos.getSTA(), prePos.getPARTNER_NAME(), prePos.getINV_DATE()));
        }

        return resultModelList;
    }

    public void updatePrePos(List<PrePosModel> prePosModelList) {

        List<PrePos> prePosList = Lists.newArrayList();

        for(PrePosModel prePosModel : prePosModelList) {
            prePosList.add(prePosModelToDto(prePosModel));
        }

        prePosService.update(prePosList);
    }

    private PrePos prePosModelToDto(PrePosModel prePosModel) {
        return new PrePos(prePosModel.getTYPE(), prePosModel.getSTA(), prePosModel.getPARTNER_NAME(),
                prePosModel.getINV_DATE());
    }
}
