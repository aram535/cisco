package com.cisco.prepos.services.totalsum;

import com.cisco.prepos.model.PreposModel;

/**
 * User: Rost
 * Date: 25.05.2014
 * Time: 15:33
 */
public interface TotalSumCounter {
    double countTotalPosSum(Iterable<PreposModel> preposModels);
}
