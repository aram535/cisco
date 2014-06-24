package com.cisco.clients.service;

import com.cisco.clients.dto.Client;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 24.06.2014.
 */

@Component("clientFilter")
public class DefaultClientFilter implements ClientFilter {

	@Override
	public List<Client> filter(List<Client> clients, ClientRestrictions clientRestrictions) {

		Predicate<Client> partnerNamePredicate = getPartnerNamePredicate(clientRestrictions.getPartnerName());
		Predicate<Client> clientNumberPredicate = getClientNumberPredicate(clientRestrictions.getClientNumber());

		Collection<Client> filteredClients = Collections2.filter(clients, Predicates.and(partnerNamePredicate, clientNumberPredicate));

		List<Client> resultClients = Lists.newArrayList();
		resultClients.addAll(filteredClients);

		return resultClients;
	}

	private Predicate<Client> getPartnerNamePredicate(final String partnerName) {
		return new Predicate<Client>() {
			@Override
			public boolean apply(Client client) {
				if(StringUtils.isBlank(partnerName)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(client.getName(), partnerName);
			}
		};
	}

	private Predicate<Client> getClientNumberPredicate(final String clientNumber) {
		return new Predicate<Client>() {
			@Override
			public boolean apply(Client client) {
				if(StringUtils.isBlank(clientNumber)) {
					return true;
				}
				return StringUtils.containsIgnoreCase(client.getClientNumber(), clientNumber);
			}
		};
	}
}
