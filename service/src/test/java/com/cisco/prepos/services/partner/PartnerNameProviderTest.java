package com.cisco.prepos.services.partner;

import com.cisco.clients.dto.Client;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static com.cisco.sales.dto.SaleBuilder.builder;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 04.05.2014
 * Time: 16:16
 */
public class PartnerNameProviderTest {

    private static final String CLIENT_NUMBER = "clientNumber";
    private static final String CLIENT_NAME = "client name";
    private static final String CLIENT_NAME_FROM_SALE = "client name from sale";
    private PartnerNameProvider partnerNameProvider = new DefaultPartnerNameProvider();

    @Test
    public void thatReturnsPartnerNameFromClientsMapIfExists() {
        String partnerName = partnerNameProvider.getPartnerName(createSale(), createClientsMap());
        assertThat(partnerName).isEqualTo(CLIENT_NAME);
    }

    @Test
    public void thatReturnsPartnerNameFromSaleIfNoMatchingInClientsMap() {
        String partnerName = partnerNameProvider.getPartnerName(createSale(), Maps.<String, Client>newHashMap());
        assertThat(partnerName).isEqualTo(CLIENT_NAME_FROM_SALE);
    }

    private Map<String, Client> createClientsMap() {
        Map<String, Client> clients = Maps.newHashMap();

        Client client = new Client();
        client.setClientNumber(CLIENT_NUMBER);
        client.setName(CLIENT_NAME);

        clients.put(CLIENT_NUMBER, client);
        return clients;
    }

    private Sale createSale() {
        return builder().clientNumber(CLIENT_NUMBER).clientName(CLIENT_NAME_FROM_SALE).build();
    }
}
