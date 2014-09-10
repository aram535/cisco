package com.cisco.promos.service;

import com.cisco.promos.dto.Promo;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 10.09.2014.
 */
@Component("promosFilter")
public class DefaultPromosFilter implements PromosFilter {
	@Override
	public List<Promo> filter(List<Promo> promos, PromosRestrictions promosRestrictions) {
		Predicate<Promo> partNumberPredicate = getPartNumberPredicate(promosRestrictions.getPartNumber());

		Collection<Promo> filteredPromos = Collections2.filter(promos, Predicates.and(partNumberPredicate));

		List<Promo> resultPromos = Lists.newArrayList();
		resultPromos.addAll(filteredPromos);

		return resultPromos;
	}

	private Predicate<Promo> getPartNumberPredicate(final String partNumber) {
		return new Predicate<Promo>() {
			@Override
			public boolean apply(Promo promo) {
				if(StringUtils.isBlank(partNumber)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(promo.getPartNumber(), partNumber);
			}
		};
	}
}
