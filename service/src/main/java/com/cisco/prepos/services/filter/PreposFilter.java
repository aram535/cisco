package com.cisco.prepos.services.filter;

import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;

import java.util.List;

/**
 * User: Rost
 * Date: 18.05.2014
 * Time: 14:52
 */
public interface PreposFilter {
    List<PreposModel> filter(List<PreposModel> preposes, PreposRestrictions preposRestrictions);
}
