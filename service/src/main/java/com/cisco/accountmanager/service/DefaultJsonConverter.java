package com.cisco.accountmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<String> items = gson.fromJson(json, List.class);
        return items;
    }

    @Override
    public String toJson(List<String> items) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonRepresentation = gson.toJson(items);
        return jsonRepresentation;
    }
}
