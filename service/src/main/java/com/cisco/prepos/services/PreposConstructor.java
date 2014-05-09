package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 03.05.2014.
 */
public interface PreposConstructor {

    List<Prepos> construct(List<Sale> sales, Map<String, Client> clientsMap,
                           Map<String, Pricelist> pricelistsMap,
                           Map<String, Promo> promosMap,
                           Table<String, String, Dart> dartsTable);


}
