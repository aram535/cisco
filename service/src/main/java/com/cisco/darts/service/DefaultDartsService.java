package com.cisco.darts.service;

import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartAssistant;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
@Service("dartsService")
public class DefaultDartsService implements DartsService {

    @Autowired
    private DartsDao dartsDao;

	@Transactional
    @Override
    public List<Dart> getDarts() {
        return dartsDao.getDarts();
    }

	@Transactional
    @Override
    public void save(Dart dart) {
        dartsDao.save(dart);
    }

	@Transactional
	@Override
	public void saveAll(List<Dart> darts) {
		dartsDao.saveAll(darts);
	}

	@Transactional
    @Override
    public void update(Dart dart) {
        dartsDao.update(dart);
    }

	@Transactional
    @Override
    public void update(Collection<Dart> darts) {
        dartsDao.update(darts);
    }

	@Transactional
    @Override
    public void delete(Dart dart) {
        dartsDao.delete(dart);
    }

	@Transactional
	@Override
	public void delete(List<Dart> darts) {
		dartsDao.delete(darts);
	}

	@Transactional
    @Override
    public Table<String, String, Dart> getDartsTable() {
        List<Dart> darts = getDarts();

        return DartAssistant.dartsToTable(darts);
    }

	@Transactional
    @Override
    public void deleteAll() {
        dartsDao.deleteAll();
    }

}
