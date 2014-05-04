package com.cisco.prepos.services.partner;

import com.cisco.clients.dto.Client;
import com.cisco.sales.dto.Sale;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * User: Rost
 * Date: 04.05.2014
 * Time: 16:17
 */
@Component
public class DefaultPartnerNameProvider implements PartnerNameProvider {

    @Override
    public String getPartnerName(Sale sale, Map<String, Client> clientsMap) {

        String clientNumber = sale.getClientNumber();

        Client client = clientsMap.get(clientNumber);

        if (client != null) {
            return client.getName();
        }

        return sale.getClientName();
    }
}
