package com.cisco.clients.service;

import com.cisco.clients.dao.ClientsDao;
import com.cisco.clients.dto.Client;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 08.04.14.
 */
@Service("clientsService")
public class DefaultClientsService implements ClientsService {

    @Autowired
    ClientsDao clientsDao;

    @Override
    public List<Client> getClients() {
        return clientsDao.getClients();
    }

    @Override
    public Map<String, Client> getClientsMap() {

        List<Client> clients = clientsDao.getClients();

        return Maps.uniqueIndex(clients, new Function<Client, String>() {
            @Override
            public String apply(Client client) {
                return client.getClientNumber();
            }
        });
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

}
