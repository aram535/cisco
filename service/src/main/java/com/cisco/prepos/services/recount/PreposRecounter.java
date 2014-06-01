package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import org.javatuples.Quartet;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:05
 */
public interface PreposRecounter {
    //TODO I see reasoning tp create smth like PreposModelInfo to incapsulate suitableDarts, selectedDart, promoValid flag in it
    List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recount(List<Prepos> preposes);
}
