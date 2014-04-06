package com.cisco.services;

import com.cisco.dao.PrePosDao;
import com.cisco.dto.PrePos;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Service
public class PrePosServiceImpl implements PrePosService {

    @Autowired
    private PrePosDao prePosDao;

    private List<PrePos> testPrePosData = Lists.newArrayList(
            new PrePos("CISCO Enterprise",	383, "SPEZVUZAUTOMATIKA", Date.valueOf("2014-03-14")),
            new PrePos("CISCO SB", 42,"Brain-computers", Date.valueOf("2014-02-15")));

    @Override
    public List<PrePos> getAllData() {

        return testPrePosData;
    }

    @Override
    public void save(PrePos prePos) {

    }

    @Override
    public void update(List<PrePos> prePosList) {

        testPrePosData = prePosList;
        /*for(PrePos prePos : prePosList) {
            prePosDao.update(prePos);
        }*/

    }

    public PrePosDao getPrePosDao() {
        return prePosDao;
    }

    public void setPrePosDao(PrePosDao prePosDao) {
        this.prePosDao = prePosDao;
    }
}
