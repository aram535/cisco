package com.cisco.darts.excel;


import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.cisco.darts.dto.DartAssistant.dartsToTable;
import static com.cisco.darts.dto.DartBuilder.builder;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDartsImporterTest {

    public static final String SECOND_CISCO_SKU = "CiscoSku2";
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
        when(dartsService.getDartsTable()).thenReturn(HashBasedTable.<String, String, Dart>create());
        when(dartsExtractor.extract(inputStream)).thenReturn(createExpectedDartsWithClones());
        defaultDartsImporter.importDarts(inputStream);

        verify(dartsService).saveAll(createExpectedDarts());
    }

    @Test
    public void importedSameDartsWithSameVersionMakeNoChanges() {
        when(dartsExtractor.extract(inputStream)).thenReturn(dartsWithSameVersionWithSameQuantity());
        when(dartsService.getDartsTable()).thenReturn(dartsToTable(existingDarts()));
        defaultDartsImporter.importDarts(inputStream);

        verify(dartsService).update(getEmptyDarts());
        verify(dartsService).saveAll(getEmptyDarts());
    }

    private ArrayList<Dart> getEmptyDarts() {
        return Lists.newArrayList();
    }

    @Test
    public void ifCompletelyNewDartsIsImportedNoExistingWereDeleted() {
        List<Dart> completelyNewDarts = completelyNewDarts();
        when(dartsExtractor.extract(inputStream)).thenReturn(completelyNewDarts);
        when(dartsService.getDartsTable()).thenReturn(dartsToTable(createExpectedDarts()));

        defaultDartsImporter.importDarts(inputStream);

        verify(dartsService).saveAll(completelyNewDarts);
        verify(dartsService).update(getEmptyDarts());
    }

    @Test
    public void quantityIsRecountedCorrectlyWhenDartsWithNewVersionImported() {
        when(dartsExtractor.extract(inputStream)).thenReturn(dartsWithNewVersionWithDifferentQuantity());
        when(dartsService.getDartsTable()).thenReturn(dartsToTable(existingDarts()));
        defaultDartsImporter.importDarts(inputStream);

        List<Dart> existingDarts = existingDarts();
        List<Dart> importedDarts = dartsWithNewVersionWithDifferentQuantity();

        Dart firstDart = importedDarts.get(0);
        Dart existingFirstDart = existingDarts.get(0);
        int recountedQuantity = existingFirstDart.getQuantity() + (firstDart.getQuantityInitial() - existingFirstDart.getQuantityInitial());
        firstDart.setQuantity(recountedQuantity);

        verify(dartsService).update(importedDarts);
        verify(dartsService).saveAll(getEmptyDarts());
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

        return newArrayList(firstDart, secondDart);
    }

    private List<Dart> existingDarts() {

        Dart firstDart = newDart();
        firstDart.setQuantity(1);

        Dart secondDart = newDart();
        secondDart.setCiscoSku(SECOND_CISCO_SKU);

        return newArrayList(firstDart, secondDart);
    }

    private List<Dart> dartsWithSameVersionWithSameQuantity() {

        Dart firstDart = newDart();

        Dart secondDart = newDart();
        secondDart.setCiscoSku(SECOND_CISCO_SKU);

        return newArrayList(firstDart, secondDart);
    }

    private List<Dart> dartsWithNewVersionWithDifferentQuantity() {

        Dart firstDart = newDart();
        firstDart.setVersion(firstDart.getVersion() + 1);
        firstDart.setQuantityInitial(10);

        Dart secondDart = newDart();
        secondDart.setVersion(secondDart.getVersion() + 1);
        secondDart.setCiscoSku(SECOND_CISCO_SKU);

        return newArrayList(firstDart, secondDart);
    }

    private List<Dart> completelyNewDarts() {
        Dart firstDart = newDart();
        firstDart.setCiscoSku("Other sku 1");

        Dart secondDart = newDart();
        secondDart.setCiscoSku("Other sku 2");

        return newArrayList(firstDart, secondDart);
    }
}