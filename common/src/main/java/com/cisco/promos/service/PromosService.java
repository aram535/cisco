package com.cisco.promos.service;

import com.cisco.promos.dto.Promo;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
public interface PromosService {

	public List<Promo> getPromos();

	public void save(Promo promo);

	public void update(Promo promo);

	public void delete(Promo promo);
}
