package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Rost
 * Date: 09.05.2014
 * Time: 19:04
 */
@Component
public class DefaultPreposUpdater implements PreposUpdater {
    @Override
    public List<Prepos> updatePreposes(List<Prepos> preposes) {
        return Lists.newArrayList(preposes);
    }
}
