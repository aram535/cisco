package com.cisco.dao;

import com.cisco.dto.PrePos;
import org.springframework.stereotype.Repository;

/**
 * Created by Alf on 05.04.14.
 */
@Repository
public class HibernatePrePosDao implements PrePosDao {


    @Override
    public boolean delete(PrePos prePos) {
        return false;
    }

    @Override
    public boolean insert(PrePos prePos) {
        return false;
    }

    @Override
    public boolean update(PrePos prePos) {
        return false;
    }
}
