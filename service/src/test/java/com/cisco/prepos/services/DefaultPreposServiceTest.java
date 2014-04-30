package com.cisco.prepos.services;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Alf on 07.04.14.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = AppConfig.class)
public class DefaultPreposServiceTest {

    //@Autowired
    DefaultPreposService preposService = new DefaultPreposService();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getAllDataReturnsSomeShit() throws Exception {

        //List<Prepos> preposes = preposService.getAllData();

        //assertNotNull(preposes);
        //assertTrue(preposes.size() > 0);
    }

}
