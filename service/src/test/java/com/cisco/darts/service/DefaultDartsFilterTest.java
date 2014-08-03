package com.cisco.darts.service;

import com.cisco.darts.dto.Dart;
import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DefaultDartsFilterTest {

	private DartsFilter dartsFilter = new DefaultDartsFilter();

	@Test
	public void returnsEmptyListIfInputIsEmpty() {
		DartsRestrictions dartsRestrictions = new DartsRestrictions(PARTNER_NAME, END_USER_NAME, PART_NUMBER, AUTHORIZATION_NUMBER);
		List<Dart> filteredDarts = dartsFilter.filter(Lists.<Dart>newArrayList(), dartsRestrictions);
		assertThat(filteredDarts).isNotNull().isEmpty();
	}

	@Test
	@Parameters(method = "restrictions")
	public void thatReturnsListAccordingToRestrictions(DartsRestrictions restrictions) throws Exception {

		List<Dart> darts = Lists.newArrayList(newDart());

		List<Dart> filteredDarts = dartsFilter.filter(darts, restrictions);

		assertThat(filteredDarts).isNotNull().hasSize(1);
		assertThat(filteredDarts).isEqualTo(darts);
	}

	private Object[] restrictions() {
		return $(new DartsRestrictions(PARTNER_NAME, END_USER_NAME, PART_NUMBER, AUTHORIZATION_NUMBER),
				 new DartsRestrictions(null, END_USER_NAME, PART_NUMBER, AUTHORIZATION_NUMBER),
				 new DartsRestrictions(null, null, PART_NUMBER, AUTHORIZATION_NUMBER),
				 new DartsRestrictions(null, null, null, AUTHORIZATION_NUMBER),
				 new DartsRestrictions(null, null, null, null)
				);
	}
}