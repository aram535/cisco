package com.cisco.hibernate;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 20:43
 */
@Configuration
@PropertySource("classpath:unitils.properties")
public class TestPersistenceConfig {

    @Value("${database.driverClassName}")
    private String driverClassName;
    @Value("${database.url}")
    private String url;
    @Value("${database.userName}")
    private String user;
    @Value("${database.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", hibernateDialect);

                setProperty("hibernate.show_sql", "true");
                setProperty("hibernate.format_sql", "false");
                setProperty("hibernate.cache.use_second_level_cache", "false");
                setProperty("hibernate.cache.use_query_cache", "false");
                setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");

            }
        };
    }
}
