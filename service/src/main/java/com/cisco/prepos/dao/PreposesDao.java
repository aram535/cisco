package com.cisco.prepos.dao;


import com.cisco.prepos.dto.Prepos;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */

public interface PreposesDao {

    List<Prepos> getPreposes();

	List<Prepos> getPreposes(final Prepos.Status... statuses);

    void delete(Prepos prePos);

    void save(Prepos prePos);

    void update(Prepos prePos);

    void update(List<Prepos> prePos);

    void save(List<Prepos> preposList);

}
