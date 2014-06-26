package com.cisco.accountmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Rost
 * Date: 25.06.2014
 * Time: 0:24
 */
@Component
public class DefaultJsonConverter implements JsonConverter {

    private final Logger logger = LoggerFactory.getLogger(DefaultJsonConverter.class);

    @Override
    public List<String> fromJson(String json) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<String> items = gson.fromJson(json, List.class);
            if (items == null) {
                return newArrayList();
            }
            return items;
        } catch (RuntimeException ex) {
            logger.error("error during parsing json", ex);
            return newArrayList();
        }
    }

    @Override
    public String toJson(List<String> items) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonRepresentation = gson.toJson(items);
        return jsonRepresentation;
    }
}
