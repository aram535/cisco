package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 03.05.2014.
 */
public interface PreposConstructor {

    List<Prepos> construct(List<Sale> sales, Map<String, Client> clientsMap);

}
