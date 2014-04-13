package com.cisco.clients.service;


import com.cisco.clients.dto.Client;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
public interface ClientsService {

    public List<Client> getAllData();

    public void save(Client name);

    public void update(Client nameList);

	public void delete(Client selectedClientModel);
}
