package com.cisco.pricelists.service;

import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.uniqueIndex;

/**
 * Created by Alf on 19.04.2014.
 */
@Service("pricelistsService")
public class DefaultPricelistsService implements PricelistsService {

    @Autowired
    private PricelistsDao pricelistsDao;

    @Cacheable(value = "ciscoCache", key = "'pricelist'")
    @Transactional
    @Override
    public List<Pricelist> getPricelists() {
        return pricelistsDao.getPricelists();
    }

    @Override
    public Map<String, Pricelist> getPricelistsMap() {

        List<Pricelist> pricelists = getPricelists();

        return uniqueIndex(pricelists, new Function<Pricelist, String>() {
            @Override
            public String apply(Pricelist pricelist) {
                return pricelist.getPartNumber();
            }
        });
    }

    @CacheEvict(value = "ciscoCache", key = "'pricelist'")
    @Transactional
    @Override
    public void save(Pricelist pricelist) {
        pricelistsDao.save(pricelist);
    }

    @CacheEvict(value = "ciscoCache", key = "'pricelist'")
    @Transactional
    @Override
    public void update(Pricelist pricelist) {
        pricelistsDao.update(pricelist);
    }

    @CacheEvict(value = "ciscoCache", key = "'pricelist'")
    @Transactional
    @Override
    public void delete(Pricelist pricelist) {
        pricelistsDao.delete(pricelist);
    }

    @CacheEvict(value = "ciscoCache", key = "'pricelist'")
    @Transactional
    @Override
    public void deleteAll() {
        pricelistsDao.deleteAll();
    }
}
