package com.cisco.accountmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * User: Rost
 * Date: 25.06.2014
 * Time: 0:24
 */
@Component
public class DefaultJsonConverter implements JsonConverter {

    private final Logger logger = LoggerFactory.getLogger(DefaultJsonConverter.class);

    @Override
    public Set<String> fromJson(String json) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Set<String> items = gson.fromJson(json, Set.class);
            if (items == null) {
                return newHashSet();
            }
            return items;
        } catch (RuntimeException ex) {
            logger.error("error during parsing json", ex);
            return newHashSet();
        }
    }

    @Override
    public String toJson(Set<String> items) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonRepresentation = gson.toJson(items);
        return jsonRepresentation;
    }
}
