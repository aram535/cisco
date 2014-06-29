package com.cisco.darts.excel;


import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartAssistant;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import static com.cisco.darts.dto.DartBuilder.builder;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDartsImporterTest {

	@InjectMocks
	private DefaultDartsImporter defaultDartsImporter;

	@Mock
	private DartsExtractor dartsExtractor;

	@Mock
	private DartsService dartsService;

	@Mock
	private InputStream inputStream;

	@Test(expected = CiscoException.class)
	public void thatImportDartsThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {

		when(dartsExtractor.extract(inputStream)).thenReturn(null);

		defaultDartsImporter.importDarts(inputStream);
	}

	@Test
	public void thatImportedDartsContainNoClones() {
		when(dartsExtractor.extract(inputStream)).thenReturn(createExpectedDartsWithClones());
		defaultDartsImporter.importDarts(inputStream);

		verify(dartsService).saveAll(createExpectedDarts());
	}

	@Test
	public void importedDartsWithNewVersionReplaceExistingDarts() {
		when(dartsExtractor.extract(inputStream)).thenReturn(dartsWithNewVersionWithSameQuantity());
		when(dartsService.getDartsTable()).thenReturn(DartAssistant.dartsToTable(existingDarts()));
		defaultDartsImporter.importDarts(inputStream);

		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		verify(dartsService).delete(argument.capture());

		List<Dart> acctualDarts = argument.getValue();
		assertTrue(acctualDarts.containsAll(existingDarts()));

		verify(dartsService).saveAll(dartsWithNewVersionWithSameQuantity());
	}

	@Test
	public void ifCompletlyNewDartsIsImportedNoExisitingIsDeleted() {
		when(dartsExtractor.extract(inputStream)).thenReturn(dartsWithNewVersionWithDifferentQuantity());
		when(dartsService.getDartsTable()).thenReturn(DartAssistant.dartsToTable(createExpectedDarts()));

		defaultDartsImporter.importDarts(inputStream);

		verify(dartsService, never()).delete(Matchers.<List<Dart>>any());
		verify(dartsService).saveAll(dartsWithNewVersionWithDifferentQuantity());
	}

	@Test
	public void quantityIsRecountedCorrectlyWhenDartsWithNewVersionImported() {
		when(dartsExtractor.extract(inputStream)).thenReturn(dartsWithNewVersionWithDifferentQuantity());
		when(dartsService.getDartsTable()).thenReturn(DartAssistant.dartsToTable(existingDarts()));
		defaultDartsImporter.importDarts(inputStream);

		List<Dart> existingDarts = existingDarts();
		List<Dart> importedDarts = dartsWithNewVersionWithDifferentQuantity();

		Dart firstDart = importedDarts.get(0);
		firstDart.setQuantity(existingDarts.get(0).getQuantity() + (firstDart.getQuantityInitial() - existingDarts.get(0).getQuantityInitial()));

		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		verify(dartsService).delete(argument.capture());

		List<Dart> acctualDarts = argument.getValue();
		assertTrue(acctualDarts.containsAll(existingDarts));

		verify(dartsService).saveAll(importedDarts);
	}

	private List<Dart> createExpectedDartsWithClones() {

		List<Dart> darts = createExpectedDarts();
		List<Dart> dartsClone = createExpectedDarts();

		darts.addAll(dartsClone);

		return darts;
	}

	private List<Dart> createExpectedDarts() {

		long startDateMillis = new DateTime(2014, 3, 28, 0, 0, 0, 0).getMillis();
		long endDateMillis = new DateTime(2014, 7, 26, 0, 0, 0, 0).getMillis();

		Timestamp startDate = new Timestamp(startDateMillis);
		Timestamp endDate = new Timestamp(endDateMillis);

		Dart firstDart = builder().setAuthorizationNumber("MDMF-4526117-1403").setVersion(1)
				.setDistributorInfo("ERC").setStartDate(startDate).setEndDate(endDate).setDistiDiscount(0.57)
				.setResellerName("JSC NVISION-UKRAINE").setResellerCountry("UKRAINE").setResellerAcct(0)
				.setEndUserName("BRISTOL HOTEL").setEndUserCountry("UKRAINE").setQuantity(1).setCiscoSku("ACS-1900-RM-19=")
				.setListPrice(100).setClaimUnit(15).setExtCreditAmt(15).setDistiSku("")
				.setFastTrackPie(0).setIpNgnPartnerPricingEm(0).setMdmFulfillment(15.00).build();

		Dart secondDart = builder(firstDart).setQuantity(4).setCiscoSku("EHWIC-1GE-SFP-CU=").setListPrice(799).
				setClaimUnit(119.85).setExtCreditAmt(479.4).setFastTrackPie(0).setIpNgnPartnerPricingEm(0).
				setMdmFulfillment(119.85).build();

		return Lists.newArrayList(firstDart, secondDart);
	}

	private List<Dart> existingDarts() {

		Dart firstDart = newDart();
		firstDart.setQuantity(1);

		Dart secondDart = newDart();
		secondDart.setCiscoSku("CiscoSku2");

		return Lists.newArrayList(firstDart, secondDart);
	}

	private List<Dart> dartsWithNewVersionWithSameQuantity() {

		Dart firstDart = newDart();

		Dart secondDart = newDart();
		secondDart.setCiscoSku("CiscoSku2");

		return Lists.newArrayList(firstDart, secondDart);
	}

	private List<Dart> dartsWithNewVersionWithDifferentQuantity() {

		Dart firstDart = newDart();
		firstDart.setVersion(firstDart.getVersion() + 1);
		firstDart.setQuantityInitial(10);

		Dart secondDart = newDart();
		secondDart.setVersion(secondDart.getVersion() + 1);
		secondDart.setCiscoSku("CiscoSku2");

		return Lists.newArrayList(firstDart, secondDart);
	}
}