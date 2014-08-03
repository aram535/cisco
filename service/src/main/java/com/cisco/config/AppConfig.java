package com.cisco.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * User: Rost
 * Date: 28.03.14
 * Time: 21:25
 */
@Configuration
@PropertySource("classpath:prepos.properties")
@ComponentScan(basePackages = {"com.cisco"})
public class AppConfig {

}
