package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.PreposRecounter;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:14
 */
@Component
public class DefaultPreposModelConstructor implements PreposModelConstructor {

    @Autowired
    private PreposRecounter preposRecounter;

    @Override
    public List<PreposModel> construct(List<Prepos> preposes) {

        List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> preposModelTripletList = preposRecounter.recount(preposes);

        List<PreposModel> preposModels = Lists.newArrayList();
        for (Quartet<Prepos, Map<String, Dart>, Dart, Boolean> preposModelQuartet : preposModelTripletList) {

            Prepos prepos = preposModelQuartet.getValue0();
            Map<String, Dart> suitableDarts = preposModelQuartet.getValue1();
            Dart selectedDart = preposModelQuartet.getValue2();
            Boolean firstPromoValid = preposModelQuartet.getValue3();

            PreposModel preposModel = new PreposModel(prepos, suitableDarts, selectedDart);
            preposModel.setFirstPromoValid(firstPromoValid);
            preposModels.add(preposModel);
        }

        return preposModels;
    }

	@Override
	public List<Prepos> getPreposes(Collection<PreposModel> preposModels) {
		List<Prepos> preposes = Lists.newArrayList(Collections2.transform(preposModels, toPreposTransformFunction));
		return preposes;
	}

	private final Function<PreposModel, Prepos> toPreposTransformFunction = new Function<PreposModel, Prepos>() {
		@Override
		public Prepos apply(PreposModel preposModel) {
			return preposModel.getPrepos();
		}
	};


}
