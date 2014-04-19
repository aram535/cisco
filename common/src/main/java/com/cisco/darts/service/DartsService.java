package com.cisco.darts.service;

import com.cisco.darts.dto.Dart;

import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
public interface DartsService {

	List<Dart> getDarts();

	void save(Dart dart);

	void update(Dart dart);

	void delete(Dart dart);
}
