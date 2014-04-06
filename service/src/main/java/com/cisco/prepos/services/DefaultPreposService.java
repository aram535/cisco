package com.cisco.prepos.services;

import com.cisco.prepos.dao.PreposDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public class DefaultPreposService implements PreposService {

    @Autowired
    private PreposDao preposDao;

    private List<Prepos> testPrePosData = Lists.newArrayList();

    @Override
    public List<Prepos> getAllData() {

        return testPrePosData;
    }

    @Override
    public void save(Prepos prePos) {

    }

    @Override
    public void update(List<Prepos> prePosList) {
        testPrePosData = prePosList;
    }

    @PostConstruct
    public void init() {
        PreposBuilder builder = PreposBuilder.builder();
        Prepos prepos = builder.type("Type").partnerName("Some partner").shippedDate(new Date().getTime()).build();
        testPrePosData.add(prepos);
    }

    public void setPreposDao(PreposDao preposDao) {
        this.preposDao = preposDao;
    }
}
