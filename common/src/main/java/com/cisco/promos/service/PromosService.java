package com.cisco.promos.service;

import com.cisco.promos.dto.Promo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 19.04.2014.
 */
public interface PromosService {

	public List<Promo> getPromos();

	public void save(Promo promo);

	public void update(Promo promo);

	public void delete(Promo promo);

	Map<String,Promo> getPromosMap();

	void deleteAll();

    void saveAll(List<Promo> promos);
}
