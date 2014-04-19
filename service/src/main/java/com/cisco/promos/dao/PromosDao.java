package com.cisco.promos.dao;

import com.cisco.promos.dto.Promo;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
public interface PromosDao {
	List<Promo> getPromos();

	void save(Promo promo);

	void update(Promo promo);

	void delete(Promo promo);
}
