package com.cisco.serials.service;

import com.cisco.exception.CiscoException;
import com.cisco.serials.dao.SerialsDao;
import com.cisco.serials.dto.Serial;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Alf on 27.07.2014.
 */
@Component("serialsService")
public class DefaultSerialsService implements SerialsService {

    @Autowired
    private SerialsDao serialsDao;

    @CacheEvict(value = "ciscoCache", key = "'serials'")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveOrUpdate(List<Serial> serials) {

        if (serials == null || serials.isEmpty()) {
            throw new CiscoException("Serials list is empty");
        }

        serialsDao.saveOrUpdate(serials);
    }

    @Cacheable(value = "ciscoCache", key = "'serials'")
    @Override
    public List<Serial> getAllSerials() {

        return serialsDao.getAllSerials();
    }

    @Override
    public Set<String> getAllSerialsStrings() {

        List<Serial> allSerials = getAllSerials();

        Set<String> serialsStrings = Sets.newHashSet();
        for (Serial serial : allSerials) {
            serialsStrings.add(serial.getSerialNumber());
        }

        return serialsStrings;
    }
}
