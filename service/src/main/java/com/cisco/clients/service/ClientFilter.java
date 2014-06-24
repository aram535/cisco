package com.cisco.clients.service;

import com.cisco.clients.dto.Client;

import java.util.List;

/**
 * Created by Alf on 24.06.2014.
 */
public interface ClientFilter {

	List<Client> filter(List<Client> preposes, ClientRestrictions preposRestrictions);
}
