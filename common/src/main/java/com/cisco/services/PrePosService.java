package com.cisco.services;

import com.cisco.dto.PrePos;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
public interface PrePosService  {

    public List<PrePos> getAllData();

    public void save(PrePos prePos);

    public void update(List<PrePos> prePosList);
}
