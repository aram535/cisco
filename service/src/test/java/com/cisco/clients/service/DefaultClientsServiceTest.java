package com.cisco.clients.service;

import com.cisco.clients.dao.ClientsDao;
import com.cisco.clients.dto.Client;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 08.04.14.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultClientsServiceTest {

    public static final String FIRST_CLIENT_NUMBER = "331";
    public static final String SECOND_CLIENT_NUMBER = "332";
    @InjectMocks
    private ClientsService clientsService = new DefaultClientsService();

    @Mock
    private ClientsDao clientsDao;
    private Client firstClient;
    private Client secondClient;


    private List<Client> getTestNamesData() {
        firstClient = new Client(1, FIRST_CLIENT_NUMBER, "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
        secondClient = new Client(2, SECOND_CLIENT_NUMBER, "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");

        return Lists.newArrayList(firstClient, secondClient);
    }

    @Test
    public void getClientsMapReturnsMapAccordingToDao() {
        List<Client> testNamesData = getTestNamesData();
        when(clientsDao.getClients()).thenReturn(testNamesData);

        Map<String, Client> clientsMap = clientsService.getClientsMap();

        assertThat(clientsMap).isNotNull().isNotEmpty();
        assertThat(clientsMap).hasSize(2);
        assertThat(clientsMap.keySet()).contains(FIRST_CLIENT_NUMBER, SECOND_CLIENT_NUMBER);
        assertThat(clientsMap.values()).contains(firstClient, secondClient);
    }

    @Test
    public void getAllDataReturnsDataCorrectly() throws Exception {

        List<Client> testNamesData = getTestNamesData();
        when(clientsDao.getClients()).thenReturn(testNamesData);

        List<Client> resultData = clientsService.getClients();

        assertThat(resultData).isNotNull().isNotEmpty();
        assertThat(resultData).hasSize(2);
        assertThat(resultData).isEqualTo(testNamesData);
    }
}
