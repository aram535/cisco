package com.cisco.clients.service;

import com.cisco.clients.dto.Client;
import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.cisco.testtools.TestObjects.CLIENT_NUMBER;
import static com.cisco.testtools.TestObjects.ClientsFactory.newClient;
import static com.cisco.testtools.TestObjects.PARTNER_NAME;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DefaultClientFilterTest {

	private DefaultClientFilter clientFilter = new DefaultClientFilter();

	@Test
	public void returnsEmptyListIfInputIsEmpty() {
		ClientRestrictions clientRestrictions = new ClientRestrictions(CLIENT_NUMBER, PARTNER_NAME);
		List<Client> filteredClientes = clientFilter.filter(Lists.<Client>newArrayList(), clientRestrictions);
		assertThat(filteredClientes).isNotNull().isEmpty();
	}

	@Test
	@Parameters(method = "luckyRestrictions")
	public void returnsListsAccordingToLuckyRestrictions(ClientRestrictions restrictions) {
		Client client = newClient();
		List<Client> clients = Lists.newArrayList(client);
		List<Client> filteredClients = clientFilter.filter(clients, restrictions);
		assertThat(filteredClients).isNotNull().hasSize(1);
		assertThat(filteredClients).isEqualTo(clients);
	}

	@Test
	@Parameters(method = "breakingRestrictions")
	public void returnsListsAccordingToBreakingRestrictions(ClientRestrictions restrictions) {
		Client client = newClient();
		List<Client> clients = Lists.newArrayList(client);
		List<Client> filteredClients = clientFilter.filter(clients, restrictions);
		assertThat(filteredClients).isNotNull().hasSize(0);
	}

	private Object[] luckyRestrictions() {
		return $(new ClientRestrictions(CLIENT_NUMBER, PARTNER_NAME),
				new ClientRestrictions(CLIENT_NUMBER, null),
				new ClientRestrictions(null, PARTNER_NAME),
				new ClientRestrictions(null, null));
	}

	private Object[] breakingRestrictions() {
		return $(new ClientRestrictions("some other number", "some other name"),
				new ClientRestrictions(CLIENT_NUMBER, "some other name"),
				new ClientRestrictions("some other number", PARTNER_NAME));
	}
}