package com.cisco.names.dao;

import com.cisco.names.dto.Names;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */

public interface NamesDao {

    public boolean delete(Names prePos);

    public boolean insert(Names prePos);

    public boolean update(Names prePos);

    public List<Names> getAll();

}
