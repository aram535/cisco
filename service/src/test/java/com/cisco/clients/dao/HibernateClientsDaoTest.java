package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;
import com.cisco.hibernate.BasicDb;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Alf on 14.04.14.
 */
public class HibernateClientsDaoTest extends BasicDb{

	@SpringBeanByType
	private ClientsDao clientsDao;

	@Test
	@DataSet("clients.xml")
	public void thatGetClientsReturnsAllFromDb() {
		List<Client> clients = clientsDao.getClients();
		assertThat(clients).isNotEmpty();
		assertThat(clients).containsExactly(createExpectedClient());
	}

	@Test
	@DataSet("clients.xml")
	public void thatSaveAddsDataToDb() {
		Client client = createNewClient();
		clientsDao.save(client);

		List<Client> clients = clientsDao.getClients();
		assertThat(clients).contains(client);
	}

	@Test
	@DataSet("clients.xml")
	public void thatUpdateAmmendsDataInDb() throws Exception {
		Client client = createExpectedClient();
		client.setCity("Kharkov");
		clientsDao.update(client);

		List<Client> clients = clientsDao.getClients();
		assertThat(clients).containsExactly(client);
	}

	@Test
	@DataSet("clients.xml")
	public void thatDeleteRemovesDataFromDB() throws Exception {
		Client client = createExpectedClient();
		clientsDao.delete(client);

		List<Client> clients = clientsDao.getClients();
		assertThat(clients).isEmpty();

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

	private Client createNewClient() {
		Client client = new Client();
		client.setId(2L);
		client.setClientNumber("2");
		client.setName("client");
		client.setCity("Odessa");
		client.setAddress("str.Street 6b");
		return client;
	}
}
