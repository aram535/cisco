package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.ClientsFactory.newClientMap;
import static com.cisco.testtools.TestObjects.PARTNER_NAME;
import static com.cisco.testtools.TestObjects.PreposFactory.newSimplePrepos;
import static com.cisco.testtools.TestObjects.SalesFactory.newSaleList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 10.05.2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposConstructorTest {

    @InjectMocks
    private DefaultPreposConstructor preposConstructor = new DefaultPreposConstructor();

    @Mock
    private PartnerNameProvider partnerNameProvider;

    @Mock
    private ClientsService clientsService;

    private Map<String, Client> clientsMap = newClientMap();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        when(clientsService.getClientsMap()).thenReturn(clientsMap);
    }

    @Test
    public void thatConstructReturnsEmptyPreposListIfNoNewSales() throws Exception {

        List<Sale> sales = Lists.newArrayList();

        List<Prepos> expectedPreposes = Lists.newArrayList();
        List<Prepos> actualPreposes = preposConstructor.construct(sales);

        assertThat(expectedPreposes).isEqualTo(actualPreposes);

    }

    @Test
    public void thatConstructReturnsCorrectlyBuiltPreposFromNewSales() throws Exception {

        List<Sale> sales = newSaleList();

        when(partnerNameProvider.getPartnerName(Iterables.getOnlyElement(sales), clientsMap, Lists.<String>newArrayList())).thenAnswer(new Answer<String>() {
	        @Override
	        public String answer(InvocationOnMock invocation) throws Throwable {
		        Object[] args = invocation.getArguments();
		        List<String> missingClients = (List<String>) args[2];
		        missingClients.add(PARTNER_NAME);
		        return PARTNER_NAME;
	        }
        });

	    expectedException.expect(CiscoException.class);
	    expectedException.expectMessage(String.format("Client with number:%s was not found", PARTNER_NAME));

        preposConstructor.construct(sales);
    }

	@Test
	public void throwsExceptionWhenClientNamesAreMissing() throws Exception {

		List<Sale> sales = newSaleList();

		when(partnerNameProvider.getPartnerName(Iterables.getOnlyElement(sales), clientsMap)).thenReturn(PARTNER_NAME);

		List<Prepos> expectedPreposes = Lists.newArrayList(newSimplePrepos());
		List<Prepos> actualPreposes = preposConstructor.construct(sales);

		assertThat(expectedPreposes).isEqualTo(actualPreposes);

	}
}
