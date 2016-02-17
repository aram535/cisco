package com.cisco.prepos.services.partner;

import com.cisco.clients.dto.Client;
import com.cisco.exception.CiscoException;
import com.cisco.sales.dto.Sale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 04.05.2014
 * Time: 16:17
 */
@Component
public class DefaultPartnerNameProvider implements PartnerNameProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getPartnerName(Sale sale, Map<String, Client> clientsMap) {

        String clientNumber = sale.getClientNumber();

        Client client = clientsMap.get(clientNumber);

        if (client != null) {
            return client.getName();
        } else {
	        String errorMsg = String.format("Client with number:%s was not found", clientNumber);
	        logger.error(errorMsg);
	        throw new CiscoException(errorMsg);
        }
    }

	@Override
	public String getPartnerName(Sale sale, Map<String, Client> clientsMap, List<String> missingClients) {

		String clientNumber = sale.getClientNumber();

		Client client = clientsMap.get(clientNumber);

		if (client != null) {
			return client.getName();
		} else {
			missingClients.add(clientNumber);
		}

		return sale.getClientName();
	}
}
