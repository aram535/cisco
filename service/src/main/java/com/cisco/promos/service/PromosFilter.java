package com.cisco.promos.service;

import com.cisco.promos.dto.Promo;

import java.util.List;

/**
 * Created by Alf on 10.09.2014.
 */
public interface PromosFilter {

	List<Promo> filter(List<Promo> preposes, PromosRestrictions preposRestrictions);
}
