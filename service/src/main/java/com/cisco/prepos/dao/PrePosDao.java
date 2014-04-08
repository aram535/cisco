package com.cisco.prepos.dao;


import com.cisco.prepos.dto.Prepos;

/**
 * Created by Alf on 05.04.14.
 */

public interface PreposDao {

    public boolean delete(Prepos prePos);

    public boolean insert(Prepos prePos);

    public boolean update(Prepos prePos);

}
