package com.cisco.posready.service;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 03.07.2014.
 */
public interface PosreadyService {

	public static final String posreadyFilePrefix = "posready_";

	public String exportPosready(List<Prepos> preposes, Map<String, Client> clientMap,
	                             Map<String, Pricelist> pricelistsMap,
	                             Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap);
}
