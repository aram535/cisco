package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.cisco.darts.dto.DartAssistant.EMPTY_DART;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:40
 */
@Component
public class DefaultPreposValidator implements PreposValidator {

    @Override
    public void validateDartQuantity(List<PreposModel> preposModels, PreposModel selectedModel) {

        boolean dartHasNotEnoughQuantity = !dartHasEnoughQuantity(preposModels, selectedModel);
        if (dartHasNotEnoughQuantity) {
            throw new CiscoException("Not enough quantity left in Dart. Review other preposes where current Dart is selected");
        }

    }

    private boolean dartHasEnoughQuantity(List<PreposModel> preposModels, PreposModel selectedModel) {

        Dart selectedDart = selectedModel.getSelectedDart();

        if (selectedDart == EMPTY_DART) {
            return true;
        }

        int totalQuantity = 0;

        for (PreposModel preposModel : preposModels) {

            if (selectedDart.equals(preposModel.getSelectedDart())) {
                Prepos prepos = preposModel.getPrepos();
                int saleQuantity = prepos.getQuantity();
                totalQuantity += saleQuantity;
            }

        }

        return selectedDart.getQuantity() >= totalQuantity;
    }
}
