package com.cisco.darts.dao;

import com.cisco.darts.dto.Dart;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
public interface DartsDao {

    List<Dart> getDarts();

    void save(Dart dart);

    void update(Dart dart);

    void update(Collection<Dart> darts);

    void delete(Dart dart);

    void delete(List<Dart> dart);

    int deleteAll();

    void saveAll(List<Dart> darts);
}
