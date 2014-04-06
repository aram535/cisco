package com.cisco.dao;


import com.cisco.dto.PrePos;

/**
 * Created by Alf on 05.04.14.
 */

public interface PrePosDao {

    public boolean delete(PrePos prePos);

    public boolean insert(PrePos prePos) ;

    public boolean update(PrePos prePos) ;
}
