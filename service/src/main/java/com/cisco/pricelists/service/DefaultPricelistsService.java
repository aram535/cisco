package com.cisco.pricelists.service;

import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
@Service("pricelistsService")
public class DefaultPricelistsService implements PricelistsService {

	@Autowired
	private PricelistsDao pricelistsDao;


	@Override
	public List<Pricelist> getPricelists() {
		return pricelistsDao.getPricelists();
	}

	@Override
	public void save(Pricelist pricelist) {
		pricelistsDao.save(pricelist);
	}

	@Override
	public void update(Pricelist pricelist) {
		pricelistsDao.update(pricelist);
	}

	@Override
	public void delete(Pricelist pricelist) {
		pricelistsDao.delete(pricelist);
	}
}
