package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:14
 */
@Component
public class DefaultPreposModelConstructor implements PreposModelConstructor {

    @Override
    public List<PreposModel> constructPreposModels(List<Prepos> preposes, Table<String, String, Dart> dartsTable) {
        return Lists.newArrayList();
    }
}
