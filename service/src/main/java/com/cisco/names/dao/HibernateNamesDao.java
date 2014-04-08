package com.cisco.names.dao;

import com.cisco.names.dto.Names;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
@Repository
public class HibernateNamesDao implements NamesDao {
    @Override
    public boolean delete(Names prePos) {
        return false;
    }

    @Override
    public boolean insert(Names prePos) {
        return false;
    }

    @Override
    public boolean update(Names prePos) {
        return false;
    }

    @Override
    public List<Names> getAll() {
        return null;
    }
}
