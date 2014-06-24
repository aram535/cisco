package com.cisco.accountmanager.service;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 23:33
 */
public interface JsonConverter {

    List<String> fromJson(String json);

    String toJson(List<String> items);
}
