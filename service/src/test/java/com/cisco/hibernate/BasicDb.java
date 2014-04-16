package com.cisco.hibernate;

import com.cisco.config.PersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 20:33
 */
public class BasicDb {

    @SpringApplicationContext
    public ConfigurableApplicationContext createApplicationContext() {
        return new AnnotationConfigApplicationContext(PersistenceConfig.class, TestPersistenceConfig.class);
    }

}
