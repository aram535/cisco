package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;

import java.util.List;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 21:59
 */
public interface ClientsDao {
    List<Client> getClients();
	void save(Client client);
	void update(Client client);

	void delete(Client client);

}
