package com.cisco.pricelists.service;

import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 19.04.2014.
 */
@Service("pricelistsService")
public class DefaultPricelistsService implements PricelistsService {

    @Autowired
    private PricelistsDao pricelistsDao;

	@Transactional
    @Override
    public List<Pricelist> getPricelists() {
        return pricelistsDao.getPricelists();
    }

	@Transactional
    @Override
    public Map<String, Pricelist> getPricelistsMap() {

        List<Pricelist> pricelists = pricelistsDao.getPricelists();

        return Maps.uniqueIndex(pricelists, new Function<Pricelist, String>() {
            @Override
            public String apply(Pricelist pricelist) {
                return pricelist.getPartNumber();
            }
        });
    }

	@Transactional
	@Override
    public void save(Pricelist pricelist) {
        pricelistsDao.save(pricelist);
    }

	@Transactional
    @Override
    public void update(Pricelist pricelist) {
        pricelistsDao.update(pricelist);
    }

	@Transactional
    @Override
    public void delete(Pricelist pricelist) {
        pricelistsDao.delete(pricelist);
    }

	@Transactional
	@Override
	public void deleteAll() {
		pricelistsDao.deleteAll();
	}
}
