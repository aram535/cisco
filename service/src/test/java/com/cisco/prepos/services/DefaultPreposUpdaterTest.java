package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.cisco.testtools.TestObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.SalesFactory.newSale;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 10.05.2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposUpdaterTest {

	@InjectMocks
	DefaultPreposUpdater preposUpdater = new DefaultPreposUpdater();

	@Mock
	private SalesService salesService;

	@Test
	public void thatPreposUpdater() throws Exception {

		Table<String, String, Sale> salesTable = HashBasedTable.create();
		Sale sale = newSale();
		sale.setSerials(SERIALS);
		salesTable.put(PART_NUMBER, SHIPPED_BILL_NUMBER, sale);

		Prepos notUpdatedPrepos = TestObjects.PreposFactory.newPrepos();
		notUpdatedPrepos.setSerials("");

		when(salesService.getSalesTable()).thenReturn(salesTable);

		List<Prepos> updatedPreposes = preposUpdater.updatePreposes(Lists.newArrayList(notUpdatedPrepos));

		assertThat(updatedPreposes).isEqualTo(createExpectedPreposes());
	}

	private List<Prepos> createExpectedPreposes() {
		return TestObjects.PreposFactory.newPreposList();
	}
}
