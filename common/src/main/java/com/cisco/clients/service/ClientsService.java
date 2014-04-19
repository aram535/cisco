package com.cisco.clients.service;


import com.cisco.clients.dto.Client;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
public interface ClientsService {

    public List<Client> getClients();

    public void save(Client client);

    public void update(Client client);

	public void delete(Client client);
}
