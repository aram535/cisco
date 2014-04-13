package com.cisco.names;

import com.cisco.clients.ClientsViewModel;
import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.clients.service.DefaultClientsService;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 07.04.14.
 */

public class ClientsViewModelTest {

    ClientsViewModel clientsViewModel = new ClientsViewModel();

	private List<Client> getTestNamesData() {
		Client client1 = new Client(1, "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
		Client client2 = new Client(2, "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");
		Client client3 = new Client(3, "333", "SPEZVUZAUTOMATIKA", "ODESSA", "str. Dyuka 3a");

		return Lists.newArrayList(client1, client2, client3);
	}

	@Test
	public void getAllDataReturnsDataCorrectly() throws Exception {

		//Arrange
		ClientsService mockService = mock(DefaultClientsService.class);
		when(mockService.getAllData()).thenReturn(getTestNamesData());
		clientsViewModel.setClientsService(mockService);
		//Act
		List<Client> resultData = clientsViewModel.getAllClients();

		//Assert
		assertNotNull("getAllData() result should not be null", resultData);
		assertEquals(3, resultData.size());
		assertEquals("KHARKOV", resultData.get(0).getCity());
	}

}
