package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import org.javatuples.Triplet;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:05
 */
public interface PreposRecounter {
    List<Triplet<Prepos, Map<String, Dart>, Dart>> recount(List<Prepos> preposes);
}
