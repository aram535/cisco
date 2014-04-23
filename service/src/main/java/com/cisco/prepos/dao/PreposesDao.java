package com.cisco.prepos.dao;


import com.cisco.prepos.dto.Prepos;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */

public interface PreposesDao {

	public List<Prepos> getPreposes();

    public void delete(Prepos prePos);

    public void save(Prepos prePos);

    public void update(Prepos prePos);

}
