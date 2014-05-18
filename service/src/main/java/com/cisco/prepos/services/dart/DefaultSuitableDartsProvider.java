package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

import static com.cisco.darts.dto.DartConstants.BLANK_AUTHORIZATION_NUMBER;
import static com.cisco.darts.dto.DartConstants.EMPTY_DART;

/**
 * User: Rost
 * Date: 06.05.2014
 * Time: 0:36
 */
@Component
public class DefaultSuitableDartsProvider implements SuitableDartsProvider {

    private final Comparator<String> emptyLastComparator = new Comparator<String>() {
        @Override
        public int compare(String left, String right) {
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            return left.compareTo(right);
        }
    };

    @Override
    public Map<String, Dart> getDarts(String partNumber, final String partnerName, final int quantity, final Timestamp saleDate, Table<String, String, Dart> dartsTable) {

        Map<String, Dart> dartsMapWitEmptyDart = ImmutableMap.of(BLANK_AUTHORIZATION_NUMBER, EMPTY_DART);

        if (dartsTable == null || dartsTable.isEmpty()) {
            return dartsMapWitEmptyDart;
        }

        Map<String, Dart> suitableByPartNumberDartsMap = dartsTable.row(partNumber);
        Collection<Dart> suitableByPartNumberDarts = suitableByPartNumberDartsMap.values();
        if (CollectionUtils.isEmpty(suitableByPartNumberDarts)) {
            return dartsMapWitEmptyDart;
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

        TreeMap<String, Dart> filteredDartsMap = new TreeMap(emptyLastComparator);

        filteredDartsMap.putAll(new TreeMap(Maps.uniqueIndex(filteredDarts, new Function<Dart, String>() {
            @Override
            public String apply(Dart dart) {
                return dart.getAuthorizationNumber();
            }
        })));

        filteredDartsMap.putAll(dartsMapWitEmptyDart);

        return filteredDartsMap;
    }

}
