package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.google.common.collect.Lists;
import org.javatuples.Triplet;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
public class DefaultPreposRecounter implements PreposRecounter {

    @Override
    public List<Triplet<Prepos, Map<String, Dart>, Dart>> recount(List<Prepos> preposes) {
        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }
        return null;
    }
}
