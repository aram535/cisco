package com.cisco.darts.excel;


import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import static com.cisco.darts.dto.DartBuilder.builder;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDartsImporterTest {

	@InjectMocks
	private DefaultDartsImporter defaultDartsImporter;

	@Mock
	private DartsExtractor dartsExtractor;

	@Mock
	private DartsDao dartsDao;

	@Mock
	private InputStream inputStream;

	@Test(expected = CiscoException.class)
	public void thatImportDartsThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {

		when(dartsExtractor.extract(inputStream)).thenReturn(null);

		defaultDartsImporter.importDarts(inputStream);
	}

	@Test
	public void thatImportDartsRewritesAllDartsFromDb() {

		when(dartsExtractor.extract(inputStream)).thenReturn(createExpectedDarts());
		defaultDartsImporter.importDarts(inputStream);

		verify(dartsDao).saveAll(createExpectedDarts());
		verifyNoMoreInteractions(dartsDao);
	}

	@Test
	public void thatImportedDartsContainNoClones() {
		when(dartsExtractor.extract(inputStream)).thenReturn(createExpectedDartsWithClones());
		defaultDartsImporter.importDarts(inputStream);

		verify(dartsDao).saveAll(createExpectedDarts());
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

}