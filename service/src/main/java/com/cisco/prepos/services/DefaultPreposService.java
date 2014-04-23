package com.cisco.prepos.services;

import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public class DefaultPreposService implements PreposService {

    @Autowired
    private PreposesDao preposesDao;

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
	    long millisOfSomeDate = new DateTime(2014, 3, 14, 0, 0, 0, 0).getMillis();
	    Timestamp someDate = new Timestamp(millisOfSomeDate);

        PreposBuilder builder = PreposBuilder.builder();
        Prepos prepos = builder.type("Type").partnerName("Some partner").shippedDate(someDate).build();
        testPrePosData.add(prepos);
    }

    public void setPreposesDao(PreposesDao preposesDao) {
        this.preposesDao = preposesDao;
    }
}
