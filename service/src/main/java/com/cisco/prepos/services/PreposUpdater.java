package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;

import java.util.List;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 1:15
 */
public interface PreposUpdater {

    List<Prepos> update(List<Prepos> preposes);

}
