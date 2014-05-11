package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:36
 */
@Component
public class DefaultSuitableDartsProvider implements SuitableDartsProvider {

    @Override
    public Map<String, Dart> getDarts(String partNumber, final String partnerName, final int quantity, final Timestamp saleDate, Table<String, String, Dart> dartsTable) {

        Map<String, Dart> darts = Maps.newHashMap();

        if (dartsTable == null || dartsTable.isEmpty()) {
            return darts;
        }

        Map<String, Dart> suitableByPartNumberDartsMap = dartsTable.row(partNumber);
        Collection<Dart> suitableByPartNumberDarts = suitableByPartNumberDartsMap.values();
        if (CollectionUtils.isEmpty(suitableByPartNumberDarts)) {
            return darts;
        }

        List<Dart> filteredDarts = Lists.newArrayList(Collections2.filter(suitableByPartNumberDarts, new Predicate<Dart>() {
            @Override
            public boolean apply(Dart dart) {

                String resellerName = dart.getResellerName();
                int dartQuantity = dart.getQuantity();

                boolean nameSuits = partnerName.equals(resellerName);
                boolean quantitySuits = dartQuantity >= quantity;
				boolean dateSuits = dart.getStartDate().before(saleDate) && dart.getEndDate().after(saleDate);

                return nameSuits && quantitySuits && dateSuits;
            }
        }));


        return new TreeMap(Maps.uniqueIndex(filteredDarts, new Function<Dart, String>() {
            @Override
            public String apply(Dart dart) {
                return dart.getAuthorizationNumber();
            }
        }));
    }
}
