package com.cisco.darts.dao;

import com.cisco.darts.dto.Dart;

import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
public interface DartsDao {

	List<Dart> getDarts();
	void save(Dart client);
	void update(Dart client);

	void delete(Dart client);
}
