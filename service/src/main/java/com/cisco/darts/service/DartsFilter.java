package com.cisco.darts.service;

import com.cisco.darts.dto.Dart;

import java.util.List;

/**
 * Created by Alf on 03.08.2014.
 */
public interface DartsFilter {

	List<Dart> filter(List<Dart> allDarts, DartsRestrictions dartsRestrictions);
}
