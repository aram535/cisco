package com.cisco.prepos.services;

import com.cisco.prepos.model.PreposModel;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
public interface PreposService {

    public List<PreposModel> getAllData();

    public void update(List<PreposModel> prePos);

    public void recountPrepos(PreposModel preposModel);

}
