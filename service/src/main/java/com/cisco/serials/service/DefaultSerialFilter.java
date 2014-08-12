package com.cisco.serials.service;

import com.cisco.serials.dto.Serial;
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
 * Created by Alf on 10.08.2014.
 */
@Component("serialFilter")
public class DefaultSerialFilter implements SerialFilter {

	@Override
	public List<Serial> filter(List<Serial> allSerials, String filterSerial) {

		if (CollectionUtils.isEmpty(allSerials)) {
			return Lists.newArrayList();
		}

		Predicate<Serial> serialPredicate = getSerialPredicate(filterSerial);

		Collection<Serial> filteredSerials = Collections2.filter(allSerials,
				Predicates.and(serialPredicate));

		return Lists.newArrayList(filteredSerials);
	}

	private Predicate<Serial> getSerialPredicate(final String filterSerial) {
		return new Predicate<Serial>() {
			@Override
			public boolean apply(Serial serial) {
				if (StringUtils.isBlank(filterSerial)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(serial.getSerialNumber(), filterSerial);
			}
		};
	}
}
