package com.cisco.prepos.services.partner;

import com.cisco.clients.dto.Client;
import com.cisco.sales.dto.Sale;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 04.05.2014
 * Time: 16:14
 */
public interface PartnerNameProvider {
    String getPartnerName(Sale sale, Map<String, Client> clientsMap);

    String getPartnerName(Sale sale, Map<String, Client> clientsMap, List<String> missingClients);
}
