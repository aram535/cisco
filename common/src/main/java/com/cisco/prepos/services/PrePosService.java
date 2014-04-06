package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
public interface PreposService {

    public List<Prepos> getAllData();

    public void save(Prepos prePos);

    public void update(List<Prepos> prePosList);
}
