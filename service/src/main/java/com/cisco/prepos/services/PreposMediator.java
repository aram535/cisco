package com.cisco.prepos.services;

import com.cisco.prepos.model.PreposModel;

import java.util.List;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:37
 */
public interface PreposMediator {

    List<PreposModel> getNewPreposModels();

    List<PreposModel> getAllPreposModels();

    void save(List<PreposModel> preposModels);

    void updatePreposDiscount(PreposModel preposModel);

}
