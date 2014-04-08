package com.cisco.hibernate;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 20:43
 */
@Configuration
public class TestPersistenceConfig {

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:file:target/hsqldb/test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
                setProperty("hibernate.default_schema", "PUBLIC");

                setProperty("hibernate.show_sql", "true");
                setProperty("hibernate.format_sql", "false");
                setProperty("hibernate.cache.use_second_level_cache", "false");
                setProperty("hibernate.cache.use_query_cache", "false");
                setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
            }
        };
    }
}
