package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.ClientsFactory.newClient;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PreposFactory.newPreposList;
import static com.cisco.testtools.TestObjects.SalesFactory.newSale;
import static com.google.common.collect.HashBasedTable.create;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 10.05.2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposUpdaterTest {

    private static final String EMPTY_SERIALS = "";
    private static final String OTHER_SERIALS = "OTHER_SERIALS";

    @InjectMocks
    private DefaultPreposUpdater preposUpdater = new DefaultPreposUpdater();

    @Mock
    private SalesService salesService;

	@Mock
	private ClientsService clientsService;

	@Mock
	private PartnerNameProvider partnerNameProvider;

	@Before
	public void initMocks() {
		Table<String, String, Sale> salesTable = create();
		Sale sale = newSale();
		sale.setSerials(SERIALS);
		salesTable.put(PART_NUMBER, SHIPPED_BILL_NUMBER, sale);
		when(salesService.getSalesTable()).thenReturn(salesTable);

		Map<String, Client> clientsMap = Maps.newHashMap();
		Client client = newClient();
		clientsMap.put(sale.getClientNumber(), client);
		when(clientsService.getClientsMap()).thenReturn(clientsMap);

		when(partnerNameProvider.getPartnerName(sale, clientsMap)).thenReturn(client.getName());
	}
    @Test
    public void thatUpdatesPreposesWithEmptySerials() throws Exception {

        Prepos notUpdatedPrepos = newPrepos();
        notUpdatedPrepos.setSerials(EMPTY_SERIALS);

        List<Prepos> updatedPreposes = preposUpdater.update(Lists.newArrayList(notUpdatedPrepos));

        assertThat(updatedPreposes).isEqualTo(createExpectedUpdatedPreposes());
    }

    @Test
    public void thatNotUpdatesPreposesWithNonEmptySerials() throws Exception {

        Prepos notUpdatedPrepos = newPrepos();
        notUpdatedPrepos.setSerials(OTHER_SERIALS);

        List<Prepos> updatedPreposes = preposUpdater.update(Lists.newArrayList(notUpdatedPrepos));

        assertThat(updatedPreposes).isEqualTo(createExpectedNotUpdatedPreposes());
    }

	@Test
	public void thatUpdatesPreposesWithPartnerNameFromClients() {

		Prepos notUpdatedPrepos = newPrepos();
		notUpdatedPrepos.setPartnerName(CLIENT_NAME);

		List<Prepos> updatedPreposes = preposUpdater.update(Lists.newArrayList(notUpdatedPrepos));

		assertThat(updatedPreposes).isEqualTo(createExpectedUpdatedPreposes());
	}

    private List<Prepos> createExpectedUpdatedPreposes() {
        return newPreposList();
    }

    private List<Prepos> createExpectedNotUpdatedPreposes() {

        Prepos prepos = newPrepos();
        prepos.setSerials(OTHER_SERIALS);
        return Lists.newArrayList(prepos);
    }

}
