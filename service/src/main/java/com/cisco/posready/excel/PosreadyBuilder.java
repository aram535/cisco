package com.cisco.posready.excel;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 02.07.2014.
 */
public interface PosreadyBuilder {

	byte[] buildPosready(List<Prepos> preposes, Map<String, Client> clientsMap, Map<String
			, Pricelist> pricelistsMap,
	                       Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap);
}
