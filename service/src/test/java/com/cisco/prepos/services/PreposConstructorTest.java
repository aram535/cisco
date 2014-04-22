package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
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
    private static final String CISCO_TYPE = "CISCO SB";
    private static final String PARTNER_NAME = "Partner Name";
    private static final String CLIENT_NUMBER = "158";
    private static final String CLIENT_NAME = "Client name";
    private static final String PART_NUMBER = "SPA112";
    private static final int QUANTITY = 5;
    private static final double PRICE = 20.83;
    private static final int GPL = 25;
    private static final int SALE_DISCOUNT = 17;

    @InjectMocks
    private PreposConstructor preposConstructor = new DefaultPreposConstructor();

    @Mock
    private SalesService salesService;

    @Mock
    private ClientsService clientsService;

    @Mock
    private PricelistsService pricelistsService;

    private Sale firstSale;

    @Test
    public void thatGetPreposesReturnsEmptyListIfThereAreNoSales() {
        when(salesService.getSales(NOT_PROCESSED)).thenReturn(Lists.<Sale>newArrayList());
        List<Prepos> preposes = preposConstructor.getPreposes();
        assertThat(preposes).isNotNull().isEmpty();
    }

    @Test
    public void thatGetPreposesConstructsLuckyCase() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(createExpectedClients());
        when(pricelistsService.getPricelistsMap()).thenReturn(createExpectedPriceLists());

        List<Prepos> preposes = preposConstructor.getPreposes();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);
        assertThat(preposes).isEqualTo(createExpectedPreposes());
    }

    @Test(expected = CiscoException.class)
    public void thatGetPreposesThrowsCiscoExceptionIfNoPriceFound() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(createExpectedClients());
        when(pricelistsService.getPricelistsMap()).thenReturn(Maps.<String, Pricelist>newHashMap());

        preposConstructor.getPreposes();
    }

    @Test
    public void thatGetPreposesConstructsPreposWithClientNameFromSalesIfNoMatchingByClientNumber() {

        when(salesService.getSales(NOT_PROCESSED)).thenReturn(createExpectedSales());
        when(clientsService.getClientsMap()).thenReturn(Maps.<String, Client>newHashMap());
        when(pricelistsService.getPricelistsMap()).thenReturn(createExpectedPriceLists());

        List<Prepos> preposes = preposConstructor.getPreposes();
        assertThat(preposes).isNotNull().isNotEmpty();
        assertThat(preposes).hasSize(1);
        assertThat(preposes).isEqualTo(createExpectedPreposesForCaseWhennoMatchingByClientNumber());
    }

    private List<Sale> createExpectedSales() {

        firstSale = SaleBuilder.builder().id(1).shippedDate(CURRENT_TIME).shippedBillNumber("1267894").
                clientName(CLIENT_NAME).clientNumber(CLIENT_NUMBER).clientZip("61052").partNumber(PART_NUMBER).quantity(QUANTITY).
                serials("ASDFEFE321321").price(PRICE).ciscoType(CISCO_TYPE).comment("comment").status(NOT_PROCESSED).build();

        return Lists.newArrayList(firstSale);
    }

    private Map<String, Pricelist> createExpectedPriceLists() {

        Map<String, Pricelist> pricelistsMap = Maps.newHashMap();

        Pricelist pricelist = PricelistBuilder.newPricelistBuilder().setId(2L).setPartNumber(PART_NUMBER).
                setDescription("description").setDiscount(30).setGpl(GPL).setWpl(400).build();
        pricelistsMap.put(PART_NUMBER, pricelist);

        return pricelistsMap;
    }

    private Map<String, Client> createExpectedClients() {
        Map<String, Client> clients = Maps.newHashMap();
        Client client = new Client(1L, CLIENT_NUMBER, PARTNER_NAME, "Kiev", "Bazhana str. 36");
        clients.put(CLIENT_NUMBER, client);
        return clients;
    }

    private List<Prepos> createExpectedPreposes() {
        Prepos expectedPrepos = PreposBuilder.builder().type(CISCO_TYPE).partnerName(PARTNER_NAME).
                partNumber(PART_NUMBER).quantity(QUANTITY).salePrice(PRICE).saleDiscount(SALE_DISCOUNT).build();
        return Lists.newArrayList(expectedPrepos);
    }

    private List<Prepos> createExpectedPreposesForCaseWhennoMatchingByClientNumber() {
        Prepos expectedPrepos = PreposBuilder.builder().type(CISCO_TYPE).partnerName(CLIENT_NAME).
                partNumber(PART_NUMBER).quantity(QUANTITY).salePrice(PRICE).saleDiscount(SALE_DISCOUNT).build();
        return Lists.newArrayList(expectedPrepos);
    }
}
