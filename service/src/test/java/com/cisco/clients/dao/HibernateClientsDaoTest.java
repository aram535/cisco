package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;
import com.cisco.hibernate.BasicDb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Alf on 14.04.14.
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateClientsDaoTest extends BasicDb {

    @SpringBeanByType
    private ClientsDao clientsDao;

    @Test
    @DataSet("clients.xml")
    public void thatGetClientsReturnsAllFromDb() {
        List<Client> clients = clientsDao.getClients();

	    assertThat(clients).isNotEmpty();
	    assertThat(clients.size()).isEqualTo(3);
    }

    @Test
    @DataSet("clients.xml")
    @ExpectedDataSet("clients-save-result.xml")
    public void thatSaveAddsDataToDb() {

        Client client = createNewClient();
        clientsDao.save(client);
	    System.out.println("asdasd");
    }

    @Test
    @DataSet("clients.xml")
    @ExpectedDataSet("clients-update-result.xml")
    public void thatUpdateAmmendsDataInDb() throws Exception {
        Client client = createExpectedClient();
        client.setCity("Kharkov");
        clientsDao.update(client);
    }

    @Test
    @DataSet("clients.xml")
    public void thatDeleteRemovesDataFromDB() throws Exception {
        Client client = createExpectedClient();
        clientsDao.delete(client);

	    assertThat(clientsDao.getClients().size()).isEqualTo(2);
    }

    private Client createExpectedClient() {
        Client client = new Client();
        client.setId(1L);
        client.setClientNumber("1");
        client.setName("A");
        client.setCity("Kiev");
        client.setAddress("Bazhana av. 36 - 267");
        return client;
    }

    private Client createNewClient() {
        Client client = new Client();
        client.setClientNumber("21");
        client.setName("D");
        client.setCity("Odessa");
        client.setAddress("str.Street 6b");
        return client;
    }
}
