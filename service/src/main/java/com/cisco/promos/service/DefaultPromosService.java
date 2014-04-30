package com.cisco.promos.service;

import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

	@Override
	public Map<String, Promo> getPromosMap() {

		List<Promo> promos = promosDao.getPromos();

		return Maps.uniqueIndex(promos, new Function<Promo, String>() {
			@Override
			public String apply(Promo promo) {
				return promo.getPartNumber();
			}
		});
	}
}
