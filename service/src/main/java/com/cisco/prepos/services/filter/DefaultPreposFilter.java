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

	public static final String GOOD = "G";
	public static final String BAD = "B";

	@Override
    public List<PreposModel> filter(List<PreposModel> preposes, PreposRestrictions preposRestrictions) {

        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }

        final String partnerName = preposRestrictions.getPartnerName();
        final String shippedBillNumber = preposRestrictions.getShippedBillNumber();
        final Timestamp fromDate = preposRestrictions.getFromDate();
        final Timestamp toDate = preposRestrictions.getToDate();
		final String partNumber = preposRestrictions.getPartNumber();
	    final String ok = preposRestrictions.getOk();
	    final String accountManagerName = preposRestrictions.getAccountManagerName();
	    final String endUser = preposRestrictions.getEndUser();
	    final String serial = preposRestrictions.getSerial();


        Predicate<PreposModel> partnerNamePredicate = getPartnerNamePredicate(partnerName);
        Predicate<PreposModel> shippedBillNumberPredicate = getShippedBillNumberPredicate(shippedBillNumber);
        Predicate<PreposModel> shippedDateFromPredicate = getShippedDateFromPredicate(fromDate);
        Predicate<PreposModel> shippedDateToPredicate = getShippedDateToPredicate(toDate);
        Predicate<PreposModel> partNumberPredicate = getPartNumberPredicate(partNumber);
        Predicate<PreposModel> okPredicate = getOkPredicate(ok);
        Predicate<PreposModel> accountManagerNamePredicate = getAccMangerNamePredicate(accountManagerName);
        Predicate<PreposModel> endUserPredicate = getEndUserPredicate(endUser);
        Predicate<PreposModel> serialPredicate = getSerialPredicate(serial);


        Collection<PreposModel> filteredPreposes = Collections2.filter(preposes,
                Predicates.and(partnerNamePredicate, shippedBillNumberPredicate, shippedDateFromPredicate,
		                shippedDateToPredicate, partNumberPredicate, okPredicate, accountManagerNamePredicate,
		                endUserPredicate, serialPredicate));

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
                return shippedDate.getTime() >= fromDate.getTime();
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
                return shippedDate.getTime() <= toDate.getTime();
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

	private Predicate<PreposModel> getPartNumberPredicate(final String partNumber) {
		return new Predicate<PreposModel>() {
			@Override
			public boolean apply(PreposModel preposModel) {
				if (StringUtils.isBlank(partNumber)) {
					return true;
				}
				Prepos prepos = preposModel.getPrepos();
				return StringUtils.containsIgnoreCase(prepos.getPartNumber(), partNumber);
			}
		};
	}

	private Predicate<PreposModel> getOkPredicate(final String ok) {
		return new Predicate<PreposModel>() {
			@Override
			public boolean apply(PreposModel preposModel) {

				Prepos prepos = preposModel.getPrepos();

				if(GOOD.equals(ok)) {return prepos.getOk();}
				if(BAD.equals(ok)) {return !prepos.getOk();}

				return true;
			}
		};
	}

	private Predicate<PreposModel> getAccMangerNamePredicate(final String accountManagerName) {
		return new Predicate<PreposModel>() {
			@Override
			public boolean apply(PreposModel preposModel) {
				if (StringUtils.isBlank(accountManagerName)) {
					return true;
				}
				Prepos prepos = preposModel.getPrepos();
				return StringUtils.containsIgnoreCase(prepos.getAccountManagerName(), accountManagerName);
			}
		};
	}

	private Predicate<PreposModel> getEndUserPredicate(final String endUser) {
		return new Predicate<PreposModel>() {
			@Override
			public boolean apply(PreposModel preposModel) {
				if (StringUtils.isBlank(endUser)) {
					return true;
				}
				Prepos prepos = preposModel.getPrepos();
				return StringUtils.containsIgnoreCase(prepos.getEndUser(), endUser);
			}
		};
	}

	private Predicate<PreposModel> getSerialPredicate(final String serial) {
		return new Predicate<PreposModel>() {
			@Override
			public boolean apply(PreposModel preposModel) {
				if (StringUtils.isEmpty(serial)) {
					return true;
				}

				Prepos prepos = preposModel.getPrepos();
				if (StringUtils.isBlank(serial)) {
					return StringUtils.isBlank(prepos.getSerials());
				}

				return StringUtils.containsIgnoreCase(prepos.getSerials(), serial);
			}
		};
	}
}
