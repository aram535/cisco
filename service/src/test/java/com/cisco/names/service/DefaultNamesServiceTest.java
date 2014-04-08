package com.cisco.names.service;

import com.cisco.config.AppConfig;
import com.cisco.names.dao.HibernateNamesDao;
import com.cisco.names.dao.NamesDao;
import com.cisco.names.dto.Names;
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
public class DefaultNamesServiceTest {

    @Autowired
    private DefaultNamesService namesService;

    private List<Names> getTestNamesData() {
        Names name1 = new Names("1", "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
        Names name2 = new Names("2", "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");

        return Lists.newArrayList(name1, name2);
    }

    @Test
    public void getAllDataReturnsDataCorrectly() throws Exception {

        //Arrange
        NamesDao namesDao = mock(HibernateNamesDao.class);
        when(namesDao.getAll()).thenReturn(getTestNamesData());
        namesService.setNamesDao(namesDao);
        //Act
        List<Names> resultData = namesService.getAllData();

        //Assert
        assertNotNull("getAllData() result should not be null", resultData);
        assertEquals(2, resultData.size());
        assertEquals("str. Geroev Kosmosa 18", resultData.get(1).getAddress());
    }
}
