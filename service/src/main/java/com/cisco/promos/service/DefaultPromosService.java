package com.cisco.promos.service;

import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */

@Service("promosService")
public class DefaultPromosService implements PromosService {

	@Autowired
	PromosDao promosDao;

	@Override
	public List<Promo> getPromos() {
		return promosDao.getPromos();
	}

	@Override
	public void save(Promo promo) {
		promosDao.save(promo);
	}

	@Override
	public void update(Promo promo) {
		promosDao.update(promo);
	}

	@Override
	public void delete(Promo promo) {
		promosDao.delete(promo);
	}
}
