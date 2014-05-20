package com.cisco.prepos.services.filter;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * User: Rost
 * Date: 18.05.2014
 * Time: 15:01
 */

/**
 * Any operations on output list elements affect elements of input list
 */
@Component("preposFilter")
public class DefaultPreposFilter implements PreposFilter {

    @Override
    public List<PreposModel> filter(List<PreposModel> preposes, PreposRestrictions preposRestrictions) {

        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }

        final String partnerName = preposRestrictions.getPartnerName();
        final String shippedBillNumber = preposRestrictions.getShippedBillNumber();
        final Timestamp fromDate = preposRestrictions.getFromDate();
        final Timestamp toDate = preposRestrictions.getToDate();

        Predicate<PreposModel> partnerNamePredicate = getPartnerNamePredicate(partnerName);
        Predicate<PreposModel> shippedBillNumberPredicate = getShippedBillNumberPredicate(shippedBillNumber);
        Predicate<PreposModel> shippedDateFromPredicate = getShippedDateFromPredicate(fromDate);
        Predicate<PreposModel> shippedDateToPredicate = getShippedDateToPredicate(toDate);


        Collection<PreposModel> filteredPreposes = Collections2.filter(preposes,
                Predicates.and(partnerNamePredicate, shippedBillNumberPredicate, shippedDateFromPredicate, shippedDateToPredicate));

        List<PreposModel> result = Lists.newArrayList();
        result.addAll(filteredPreposes);

        return result;
    }

    private Predicate<PreposModel> getShippedDateFromPredicate(final Timestamp fromDate) {
        return new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                if (fromDate == null) {
                    return true;
                }
                Prepos prepos = preposModel.getPrepos();
                Timestamp shippedDate = prepos.getShippedDate();
                return shippedDate.getTime() > fromDate.getTime();
            }
        };
    }

    private Predicate<PreposModel> getShippedDateToPredicate(final Timestamp toDate) {
        return new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                if (toDate == null) {
                    return true;
                }
                Prepos prepos = preposModel.getPrepos();
                Timestamp shippedDate = prepos.getShippedDate();
                return shippedDate.getTime() < toDate.getTime();
            }
        };
    }

    private Predicate<PreposModel> getPartnerNamePredicate(final String partnerName) {
        return new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                if (StringUtils.isBlank(partnerName)) {
                    return true;
                }
                Prepos prepos = preposModel.getPrepos();
                return StringUtils.containsIgnoreCase(prepos.getPartnerName(), partnerName);
            }
        };
    }

    private Predicate<PreposModel> getShippedBillNumberPredicate(final String shippedBillNumber) {
        return new Predicate<PreposModel>() {
            @Override
            public boolean apply(PreposModel preposModel) {
                if (StringUtils.isBlank(shippedBillNumber)) {
                    return true;
                }
                Prepos prepos = preposModel.getPrepos();
                return StringUtils.containsIgnoreCase(prepos.getShippedBillNumber(), shippedBillNumber);
            }
        };
    }
}