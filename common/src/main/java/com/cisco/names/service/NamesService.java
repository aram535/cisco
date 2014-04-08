package com.cisco.names.service;


import com.cisco.names.dto.Names;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
public interface NamesService {

    public List<Names> getAllData();

    public void save(Names name);

    public void update(Names nameList);

	public void delete(Names selectedNamesModel);
}
