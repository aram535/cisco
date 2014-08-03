package com.cisco.darts.service;

import com.cisco.darts.dto.Dart;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 03.08.2014.
 */
@Component("dartsFilter")
public class DefaultDartsFilter implements DartsFilter {

	@Override
	public List<Dart> filter(List<Dart> allDarts, DartsRestrictions dartsRestrictions) {

		if (CollectionUtils.isEmpty(allDarts)) {
			return Lists.newArrayList();
		}

		final String resellerName = dartsRestrictions.getResellerName();
		final String endUserName = dartsRestrictions.getEndUserName();
		final String ciscoSku = dartsRestrictions.getCiscoSku();
		final String authNumber = dartsRestrictions.getAuthNumber();

		Predicate<Dart> resellerNamePredicate = getResellerNamePredicate(resellerName);
		Predicate<Dart> endUserNamePredicate = getEndUserNamePredicate(endUserName);
		Predicate<Dart> ciscoSkuPredicate = getCiscoSkuPredicate(ciscoSku);
		Predicate<Dart> authNumberPredicate = getAuthNumberPredicate(authNumber);

		Collection<Dart> filteredDarts = Collections2.filter(allDarts,
				Predicates.and(resellerNamePredicate, endUserNamePredicate, ciscoSkuPredicate, authNumberPredicate));

		return Lists.newArrayList(filteredDarts);
	}

	private Predicate<Dart> getResellerNamePredicate(final String resellerName) {
		return new Predicate<Dart>() {
			@Override
			public boolean apply(Dart dart) {
				if (StringUtils.isBlank(resellerName)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(dart.getResellerName(), resellerName);
			}
		};
	}

	private Predicate<Dart> getEndUserNamePredicate(final String endUserName) {
		return new Predicate<Dart>() {
			@Override
			public boolean apply(Dart dart) {
				if (StringUtils.isBlank(endUserName)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(dart.getEndUserName(), endUserName);
			}
		};
	}

	private Predicate<Dart> getCiscoSkuPredicate(final String ciscoSku) {
		return new Predicate<Dart>() {
			@Override
			public boolean apply(Dart dart) {
				if (StringUtils.isBlank(ciscoSku)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(dart.getCiscoSku(), ciscoSku);
			}
		};
	}

	private Predicate<Dart> getAuthNumberPredicate(final String authNumber) {
		return new Predicate<Dart>() {
			@Override
			public boolean apply(Dart dart) {
				if (StringUtils.isBlank(authNumber)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(dart.getAuthorizationNumber(), authNumber);
			}
		};
	}
}
