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

    void save(Dart dart);

	void saveAll(List<Dart> darts);

    void update(Dart dart);

    void update(Collection<Dart> darts);

    void delete(Dart dart);

	void delete(List<Dart> darts);

    Table<String, String, Dart> getDartsTable();

	public void deleteAll();
}
