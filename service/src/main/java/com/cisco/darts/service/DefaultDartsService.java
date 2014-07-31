package com.cisco.darts.service;

import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.cisco.darts.dto.DartAssistant.dartsToTable;

/**
 * Created by Alf on 15.04.14.
 */
@Service("dartsService")
public class DefaultDartsService implements DartsService {

    @Autowired
    private DartsDao dartsDao;

    @Cacheable(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public List<Dart> getDarts() {
        return dartsDao.getDarts();
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void save(Dart dart) {
        dartsDao.save(dart);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void saveAll(List<Dart> darts) {
        dartsDao.saveAll(darts);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void update(Dart dart) {
        dartsDao.update(dart);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void update(Collection<Dart> darts) {
        dartsDao.update(darts);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void delete(Dart dart) {
        dartsDao.delete(dart);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void delete(List<Dart> darts) {
        dartsDao.delete(darts);
    }

    @Transactional
    @Override
    public Table<String, String, Dart> getDartsTable() {
        List<Dart> darts = getDarts();

        return dartsToTable(darts);
    }

    @CacheEvict(value = "ciscoCache", key = "'darts'")
    @Transactional
    @Override
    public void deleteAll() {
        dartsDao.deleteAll();
    }

}
