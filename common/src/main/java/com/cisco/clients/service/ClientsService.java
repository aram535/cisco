package com.cisco.clients.service;


import com.cisco.clients.dto.Client;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 08.04.14.
 */
public interface ClientsService {

    List<Client> getClients();

    Map<String, Client> getClientsMap();

    void save(Client client);

    void update(Client client);

	void delete(Client client);
}
