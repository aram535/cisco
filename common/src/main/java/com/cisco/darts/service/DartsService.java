package com.cisco.darts.service;

import com.cisco.darts.dto.Dart;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
public interface DartsService {

	List<Dart> getDarts();

	List<Dart> getLatestDarts();

	void save(Dart dart);

	void update(Dart dart);

	void update(Collection<Dart> darts);

	void delete(Dart dart);

	Table<String, String, Dart> getDartsTable();
}
