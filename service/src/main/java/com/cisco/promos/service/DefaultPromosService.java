package com.cisco.promos.service;

import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 19.04.2014.
 */

@Service("promosService")
public class DefaultPromosService implements PromosService {

    @Autowired
    private PromosDao promosDao;

    @Cacheable(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public List<Promo> getPromos() {
        return promosDao.getPromos();
    }

    @CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public void save(Promo promo) {
        promosDao.save(promo);
    }

    @CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public void update(Promo promo) {
        promosDao.update(promo);
    }

    @CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public void delete(Promo promo) {
        promosDao.delete(promo);
    }

    @Override
    public Map<String, Promo> getPromosMap() {

        List<Promo> promos = getPromos();

        return Maps.uniqueIndex(promos, new Function<Promo, String>() {
            @Override
            public String apply(Promo promo) {
                return promo.getPartNumber();
            }
        });
    }

    @CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public void deleteAll() {
        promosDao.deleteAll();
    }

    @CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional
    @Override
    public void saveAll(List<Promo> promos) {
        promosDao.saveAll(promos);
    }
}
