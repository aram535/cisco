package com.cisco.prepos.services;

import com.cisco.prepos.model.PreposModel;

import java.util.List;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:39
 */
public interface PreposValidator {
    void validateDartQuantity(List<PreposModel> preposList, PreposModel preposModel);
}
