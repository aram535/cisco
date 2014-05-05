package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:36
 */
public class DefaultSuitableDartsProvider implements SuitableDartsProvider {

    @Override
    public List<Dart> getDarts(String partNumber, final String partnerName, final int quantity, Table<String, String, Dart> dartsTable) {

        List<Dart> darts = Lists.newArrayList();

        if (dartsTable == null || dartsTable.isEmpty()) {
            return darts;
        }

        Map<String, Dart> suitableByPartNumberDartsMap = dartsTable.row(partNumber);
        Collection<Dart> suitableByPartNumberDarts = suitableByPartNumberDartsMap.values();
        if (CollectionUtils.isEmpty(suitableByPartNumberDarts)) {
            return darts;
        }

        Collection<Dart> filteredDarts = Collections2.filter(suitableByPartNumberDarts, new Predicate<Dart>() {
            @Override
            public boolean apply(Dart dart) {

                String resellerName = dart.getResellerName();
                int dartQuantity = dart.getQuantity();

                boolean nameSuits = partnerName.equals(resellerName);
                boolean quantitySuits = dartQuantity >= quantity;

                return nameSuits && quantitySuits;
            }
        });

        darts.addAll(filteredDarts);
        return darts;
    }
}
