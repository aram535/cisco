package com.cisco.accountmanager.service;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Rost
 * Date: 25.06.2014
 * Time: 0:24
 */
@Component
public class DefaultJsonConverter implements JsonConverter {

    @Override
    public List<String> fromJson(String json) {
        return null;
    }

    @Override
    public String toJson(List<String> items) {
        return null;
    }
}
