package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:38
 */
@RunWith(MockitoJUnitRunner.class)
public class PreposConstructorTest {

    private static final Timestamp CURRENT_TIME = new Timestamp(DateTime.now().getMillis());
    public static final String CISCO_TYPE = "CISCO SB";
    public static final String PARTNER_NAME = "Partner Name";

    @InjectMocks
    private PreposConstructor preposConstructor = new DefaultPreposConstructor();

    @Mock
    private SalesService salesService;

    @Mock
    private ClientsService clientsService;

    private Sale firstSale;

    @Test
    public void thatGetPreposesReturnsEmptyListIfThereAreNoSales() {
        when(salesService.getSales(NOT_PROCESSED)).thenReturn(Lists.<Sale>newArrayList());
        List<Prepos> preposes = preposConstructor.getPreposes();
        assertThat(preposes).isNotNull().isEmpty();
    }

    @Ignore
    public void thatGetPreposesConstructsNeededPreposesFromSales() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(createExpectedClients());

        List<Prepos> preposes = preposConstructor.getPreposes();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);
    }

    private List<Sale> createExpectedSales() {

        firstSale = SaleBuilder.builder().id(1).shippedDate(CURRENT_TIME).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip("61052").partNumber("SPA112").quantity(5).
                serials("ASDFEFE321321").price(20.83).ciscoType(CISCO_TYPE).comment("comment").status(NOT_PROCESSED).build();

        return Lists.newArrayList(firstSale);
    }

    private Map<String, Client> createExpectedClients() {
        return Maps.newHashMap();
    }

    private List<Prepos> createExpectedPreposes() {
        Prepos expectedPrepos = PreposBuilder.builder().type(CISCO_TYPE).partnerName(PARTNER_NAME).build();
        return Lists.newArrayList(expectedPrepos);
    }
}
