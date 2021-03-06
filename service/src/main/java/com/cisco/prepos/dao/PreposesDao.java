package com.cisco.prepos.dao;


import com.cisco.prepos.dto.Prepos;

import java.util.List;

import static com.cisco.prepos.dto.Prepos.Status;

/**
 * Created by Alf on 05.04.14.
 */

public interface PreposesDao {

    List<Prepos> getPreposes(Status... statuses);

    void delete(Prepos prePos);

    void save(Prepos prePos);

    void update(Prepos prePos);

    void update(List<Prepos> prePos);

    void save(List<Prepos> preposList);

}
