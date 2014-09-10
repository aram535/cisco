package com.cisco.pricelists.service;

import com.cisco.pricelists.dto.Pricelist;

import java.util.List;

/**
 * Created by Alf on 10.09.2014.
 */
public interface PricelistFilter {
	List<Pricelist> filter(List<Pricelist> pricelists, PricelistRestrictions pricelistsRestrictions);
}
