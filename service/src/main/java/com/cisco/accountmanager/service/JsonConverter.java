package com.cisco.accountmanager.service;

import java.util.Set;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 23:33
 */
public interface JsonConverter {

    Set<String> fromJson(String json);

    String toJson(Set<String> items);
}
