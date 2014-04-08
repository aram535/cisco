package com.cisco.prepos.services;

import com.cisco.config.AppConfig;
import com.cisco.prepos.dto.Prepos;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Alf on 07.04.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class DefaultPreposServiceTest {

    @Autowired
    DefaultPreposService preposService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getAllDataReturnsSomeShit() throws Exception {

        List<Prepos> preposes = preposService.getAllData();

        assertNotNull(preposes);
        assertTrue(preposes.size() > 0);
    }

}
