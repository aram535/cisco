package com.cisco.prepos.dao;

import com.cisco.prepos.dto.Prepos;
import org.springframework.stereotype.Repository;

/**
 * Created by Alf on 05.04.14.
 */
@Repository
public class HibernatePreposDao implements PreposDao {


    @Override
    public boolean delete(Prepos prePos) {
        return false;
    }

    @Override
    public boolean insert(Prepos prePos) {
        return false;
    }

    @Override
    public boolean update(Prepos prePos) {
        return false;
    }
}
