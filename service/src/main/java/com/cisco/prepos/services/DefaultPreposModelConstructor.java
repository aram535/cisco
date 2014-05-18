package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.PreposRecounter;
import com.google.common.collect.Lists;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        List<PreposModel> preposModels = Lists.newArrayList();
        List<Triplet<Prepos, Map<String, Dart>, Dart>> preposModelTripletList = preposRecounter.recount(preposes);

        for (Triplet<Prepos, Map<String, Dart>, Dart> preposModelTriplet : preposModelTripletList) {

            Prepos prepos = preposModelTriplet.getValue0();
            Map<String, Dart> suitableDarts = preposModelTriplet.getValue1();
            Dart selectedDart = preposModelTriplet.getValue2();

            PreposModel preposModel = new PreposModel(prepos, suitableDarts, selectedDart);
            preposModels.add(preposModel);
        }

        return preposModels;
    }

    @Override
    public List<Prepos> getPreposesFromPreposModels(List<PreposModel> preposModels) {

        List<Prepos> preposes = Lists.newArrayList();

        for (PreposModel preposModel : preposModels) {
            preposes.add(preposModel.getPrepos());
        }

        return preposes;
    }
}
