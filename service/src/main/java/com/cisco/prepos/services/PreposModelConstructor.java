package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;

import java.util.Collection;
import java.util.List;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:09
 */
public interface PreposModelConstructor {

    List<PreposModel> construct(List<Prepos> preposes);

	List<Prepos> getPreposes(Collection<PreposModel> preposModels);

}
