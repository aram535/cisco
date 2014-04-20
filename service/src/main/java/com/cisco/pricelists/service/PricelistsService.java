package com.cisco.pricelists.service;

import com.cisco.pricelists.dto.Pricelist;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
public interface PricelistsService {

	public List<Pricelist> getPricelists();

	public void save(Pricelist pricelist);

	public void update(Pricelist pricelist);

	public void delete(Pricelist pricelist);
}
