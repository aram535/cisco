package com.cisco.pricelists.service;

import com.cisco.pricelists.dto.Pricelist;
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
@Component("pricelistFilter")
public class DefaultPricelistFilter implements PricelistFilter {
	@Override
	public List<Pricelist> filter(List<Pricelist> pricelists, PricelistRestrictions pricelistsRestrictions) {
		Predicate<Pricelist> partNumberPredicate = getPartNumberPredicate(pricelistsRestrictions.getPartNumber());

		Collection<Pricelist> filteredPricelists = Collections2.filter(pricelists, Predicates.and(partNumberPredicate));

		List<Pricelist> resultPricelists = Lists.newArrayList();
		resultPricelists.addAll(filteredPricelists);

		return resultPricelists;
	}

	private Predicate<Pricelist> getPartNumberPredicate(final String partNumber) {
		return new Predicate<Pricelist>() {
			@Override
			public boolean apply(Pricelist pricelist) {
				if(StringUtils.isBlank(partNumber)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(pricelist.getPartNumber(), partNumber);
			}
		};
	}
}
