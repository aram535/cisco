package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;
import com.cisco.hibernate.BasicDb;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 22:00
 */
@Ignore
public class ClientsDaoTest extends BasicDb {

    @SpringBeanByType
    private ClientsDao clientsDao;

    @Test
    @DataSet("clients.xml")
    public void thatGetClientsReturnsAllFromDb() {
        List<Client> clients = clientsDao.getClients();
        assertThat(clients).isNotEmpty();
        assertThat(clients).containsExactly(createExpectedClient());
    }

    private Client createExpectedClient() {
        Client client = new Client();
        client.setId(1L);
        client.setClientNumber("1");
        client.setName("client");
        client.setCity("Kiev");
        client.setAddress("Bazhana av. 36 - 267");
        return client;
    }
}
