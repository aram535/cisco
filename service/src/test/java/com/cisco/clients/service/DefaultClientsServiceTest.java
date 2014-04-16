package com.cisco.clients.service;

import com.cisco.clients.dao.ClientsDao;
import com.cisco.clients.dao.HibernateClientsDao;
import com.cisco.clients.dto.Client;
import com.cisco.config.AppConfig;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 08.04.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class DefaultClientsServiceTest {

    @Autowired
    private DefaultClientsService namesService;

    private List<Client> getTestNamesData() {
	    Client name1 = new Client(1, "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
	    Client name2 = new Client(2, "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");

        return Lists.newArrayList(name1, name2);
    }

    @Test
    public void getAllDataReturnsDataCorrectly() throws Exception {

        //Arrange
        ClientsDao clientsDao = mock(HibernateClientsDao.class);
        when(clientsDao.getClients()).thenReturn(getTestNamesData());
        namesService.setClientsDao(clientsDao);
        //Act
        List<Client> resultData = namesService.getAllData();

        //Assert
        assertNotNull("getAllData() result should not be null", resultData);
        assertEquals(2, resultData.size());
        assertEquals("str. Geroev Kosmosa 18", resultData.get(1).getAddress());
    }
}
