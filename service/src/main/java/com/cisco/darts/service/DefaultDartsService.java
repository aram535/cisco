package com.cisco.darts.service;

import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
@Service("dartsService")
public class DefaultDartsService implements DartsService {

	@Autowired
	DartsDao dartsDao;

	@Override
	public List<Dart> getAllDarts() {
		return dartsDao.getDarts();
	}

	@Override
	public void save(Dart dart) {
		dartsDao.save(dart);
	}

	@Override
	public void update(Dart dart) {
		dartsDao.update(dart);
	}

	@Override
	public void delete(Dart dart) {
		dartsDao.delete(dart);
	}
}
