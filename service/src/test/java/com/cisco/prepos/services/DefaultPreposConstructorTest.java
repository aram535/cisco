package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.ClientsFactory.newClientMap;
import static com.cisco.testtools.TestObjects.PARTNER_NAME;
import static com.cisco.testtools.TestObjects.PreposFactory.newSimplePrepos;
import static com.cisco.testtools.TestObjects.SalesFactory.newSaleList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 10.05.2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposConstructorTest {

	@InjectMocks
	private DefaultPreposConstructor preposConstructor = new DefaultPreposConstructor();

	@Mock
	private PartnerNameProvider partnerNameProvider;

	@Test
	public void thatConstructReturnsEmptyPreposListIfNoNewSales() throws Exception {

		List<Sale> sales = Lists.newArrayList();
		Map<String, Client> clientMap = newClientMap();

		List<Prepos> expectedPreposes = Lists.newArrayList();
		List<Prepos> actualPreposes = preposConstructor.construct(sales, clientMap);

		assertThat(expectedPreposes).isEqualTo(actualPreposes);

	}

	@Test
	public void thatConstructReturnsCorrectlyBuiltPreposFromNewSales() throws Exception {

		List<Sale> sales = newSaleList();
		Map<String, Client> clientMap = newClientMap();

		when(partnerNameProvider.getPartnerName(Iterables.getOnlyElement(sales),clientMap)).thenReturn(PARTNER_NAME);

		List<Prepos> expectedPreposes = Lists.newArrayList(newSimplePrepos());
		List<Prepos> actualPreposes = preposConstructor.construct(sales, clientMap);

		assertThat(expectedPreposes).isEqualTo(actualPreposes);

	}

}
