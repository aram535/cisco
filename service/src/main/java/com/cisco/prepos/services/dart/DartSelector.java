package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;

import java.util.Map;

/**
 * User: Rost
 * Date: 14.05.2014
 * Time: 0:09
 */
public interface DartSelector {
    Dart selectDart(Map<String, Dart> suitableDarts, String secondPromo);
}
