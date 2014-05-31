package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartConstants;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:40
 */
@Component
public class DefaultPreposValidator implements PreposValidator {

    @Override
    public void validateDartQuantity(List<PreposModel> preposModels, PreposModel selectedModel) {

	    if(!dartHaveEnoughQuantity(preposModels, selectedModel)) {
		    throw new CiscoException("Not enough quantity left in Dart. Review other preposes where current Dart is selected");
	    }

    }

	private boolean dartHaveEnoughQuantity(List<PreposModel> preposModels, PreposModel selectedModel) {

		if(selectedModel.equals(DartConstants.EMPTY_DART)) {
			return true;
		}

		int totalQuantity = 0;

		Dart selectedDart = selectedModel.getSelectedDart();

		for (PreposModel preposModel : preposModels) {

			if(selectedDart.equals(preposModel.getSelectedDart())) {
				Prepos prepos = preposModel.getPrepos();
				int saleQuantity = prepos.getQuantity();
				totalQuantity += saleQuantity;
			}

		}
		return selectedDart.getQuantity() >= totalQuantity;
	}
}
