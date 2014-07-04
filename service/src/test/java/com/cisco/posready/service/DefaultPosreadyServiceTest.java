package com.cisco.posready.service;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.posready.excel.DefaultPosreadyBuilder;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.hamcrest.core.StringStartsWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.ClientsFactory.newClient;
import static com.cisco.testtools.TestObjects.DartsFactory.getDartsTable;
import static com.cisco.testtools.TestObjects.PART_NUMBER;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static com.cisco.testtools.TestObjects.PricelistsFactory.newPricelist;
import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.google.common.collect.ImmutableMap.of;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPosreadyServiceTest {

	@InjectMocks
	private DefaultPosreadyService defaultPosreadyService = new DefaultPosreadyService();

	@Mock
	private DefaultPosreadyBuilder defaultPosreadyBuilder;

	private String posreadyFolder = "C:\\temp\\";
	{
		defaultPosreadyService.setPosreadyFolder(posreadyFolder);
	}


	private final Prepos prepos = newPrepos();

	private final Map<String, Client> clientsMap = of(prepos.getClientNumber(), newClient());
	private final Table<String, String, Dart> dartsTable = getDartsTable();
	private final Map<String, Pricelist> pricelistMap = of(prepos.getPartNumber(), newPricelist());
	private final Map<String, Promo> promosMap = of(PART_NUMBER, newPromo());



	@Test
	public void thatPosreadyFileIsSavedAndPathIsReturned() throws Exception {
		List<Prepos> preposes = Lists.newArrayList(prepos);

		when(defaultPosreadyBuilder.buildPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap)).thenReturn(new byte[1]);

		String path = defaultPosreadyService.exportPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap);

		verify(defaultPosreadyBuilder, times(1)).buildPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap);

		assertThat(path, StringStartsWith.startsWith(String.format("%sposready_", posreadyFolder)));

		File file = new File(path);
		assertTrue(file.exists());

		file.delete();
	}

	@Test
	public void thatUniqueFileNameBasedOnDateIsGenerated() throws Exception {
		List<Prepos> preposes = Lists.newArrayList(prepos);

		when(defaultPosreadyBuilder.buildPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap)).thenReturn(new byte[1]);

		String path = defaultPosreadyService.exportPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap);

		verify(defaultPosreadyBuilder, times(1)).buildPosready(preposes, clientsMap, pricelistMap, dartsTable, promosMap);

		assertThat(path, StringStartsWith.startsWith(String.format("%sposready_", posreadyFolder)));
	}


}