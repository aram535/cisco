package com.cisco.clients.service;

import com.cisco.clients.dao.ClientsDao;
import com.cisco.clients.dto.Client;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.uniqueIndex;

/**
 * Created by Alf on 08.04.14.
 */
@Service("clientsService")
public class DefaultClientsService implements ClientsService {

    @Autowired
    private ClientsDao clientsDao;

    @Transactional
    @Override
    public List<Client> getClients() {
        return clientsDao.getClients();
    }

    @Override
    public Map<String, Client> getClientsMap() {

        List<Client> clients = clientsDao.getClients();

        return uniqueIndex(clients, new Function<Client, String>() {
            @Override
            public String apply(Client client) {
                return client.getClientNumber();
            }
        });
    }

    @Transactional
    @Override
    public void save(Client client) {
        clientsDao.save(client);
    }

    @Transactional
    @Override
    public void update(Client client) {
        clientsDao.update(client);
    }

    @Transactional
    @Override
    public void delete(Client client) {
        clientsDao.delete(client);
    }


    public void setClientsDao(ClientsDao clientsDao) {
        this.clientsDao = clientsDao;
    }

}
