package com.cisco.clients.service;

import com.cisco.clients.dao.ClientsDao;
import com.cisco.clients.dto.Client;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
@Service("clientsService")
public class DefaultClientsService implements ClientsService {

    @Autowired
    ClientsDao clientsDao;

	private List<Client> serviceData = initSomeData();

    @Override
    public List<Client> getAllData() {
        return clientsDao.getClients();
	    //return serviceData;
    }

    @Override
    public void save(Client client) {
	    clientsDao.save(client);
    }

    @Override
    public void update(Client client) {
	    clientsDao.update(client);
    }

	@Override
	public void delete(Client client) {
		clientsDao.delete(client);
	}


	public void setClientsDao(ClientsDao clientsDao) {
        this.clientsDao = clientsDao;
    }

	private List<Client> initSomeData() {
		Client client1 = new Client(1, "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
		Client client2 = new Client(2, "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");
		Client client3 = new Client(3, "333", "SPEZVUZAUTOMATIKA", "ODESSA", "str. Dyuka 3a");

		return Lists.newArrayList(client1, client2, client3);
	}
}
