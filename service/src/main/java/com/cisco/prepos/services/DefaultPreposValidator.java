package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.PreposValidationException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:40
 */
@Component
public class DefaultPreposValidator implements PreposValidator {

    private final Function byDartGroupFunction = new Function<PreposModel, Dart>() {

        @Override
        public Dart apply(PreposModel preposModel) {
            return preposModel.getSelectedDart();
        }
    };

    private final Predicate quantityNotSuitsPredicate = new Predicate<Map.Entry<Dart, Collection<PreposModel>>>() {
        @Override
        public boolean apply(Map.Entry<Dart, Collection<PreposModel>> entry) {
            Dart dart = entry.getKey();
            int dartQuantity = dart.getQuantity();

            Collection<PreposModel> preposModels = entry.getValue();

            int totalQuantity = 0;
            for (PreposModel preposModel : preposModels) {
                Prepos prepos = preposModel.getPrepos();
                int saleQuantity = prepos.getQuantity();
                totalQuantity += saleQuantity;
            }
            return totalQuantity > dartQuantity;
        }
    };
    private final Function modelsToPreposesTransform = new Function<Collection<PreposModel>, Collection<Prepos>>() {
        @Override
        public Collection<Prepos> apply(Collection<PreposModel> models) {
            return Collections2.transform(models, new Function<PreposModel, Prepos>() {
                @Override
                public Prepos apply(PreposModel model) {
                    return model.getPrepos();
                }
            });
        }
    };

    @Override
    public void validate(List<PreposModel> preposModels) {
        Multimap<Dart, PreposModel> modelsByDarts = Multimaps.index(preposModels, byDartGroupFunction);
        Map<Dart, Collection<PreposModel>> dartsToModelsMap = Maps.newHashMap(modelsByDarts.asMap());
        Map<Dart, Collection<PreposModel>> notValidPreposModels = Maps.newHashMap(Maps.filterEntries(dartsToModelsMap, quantityNotSuitsPredicate));
        Map<Dart, Collection<Prepos>> dartToPreposesMap = Maps.newHashMap(Maps.transformValues(notValidPreposModels, modelsToPreposesTransform));
        if (dartToPreposesMap.size() > 0) {
            throw new PreposValidationException("validation failed", dartToPreposesMap);
        }
    }
}
