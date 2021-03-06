package com.cisco.promos.excel;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.cisco.promos.service.PromosService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 14:45
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPromosImporterTest {

    @InjectMocks
    private PromosImporter promosImporter = new DefaultPromosImporter();

    @Mock
    private PromosExtractor promosExtractor;

    @Mock
    private InputStream inputStream;

    @Mock
    private PromosService promosService;


    @Before
    public void init() {
        when(promosExtractor.extract(inputStream)).thenReturn(createExpectedPromos());
    }

    @Test(expected = CiscoException.class)
    public void thatImportPromosThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {
        when(promosExtractor.extract(inputStream)).thenReturn(null);

        promosImporter.importPromos(inputStream);
    }

    @Test
    public void thatImportPromosJustReplacedAllData() {
        promosImporter.importPromos(inputStream);

        verify(promosExtractor).extract(inputStream);
        verify(promosService).deleteAll();
        verify(promosService).saveAll(createExpectedPromos());
        verifyNoMoreInteractions(promosExtractor, promosService);
    }

    @Test
    public void thatImportedPricelistsContainNoClones() {
        when(promosExtractor.extract(inputStream)).thenReturn(createExpectedPromosWtihClones());
        promosImporter.importPromos(inputStream);

        verify(promosService).saveAll(createExpectedPromos());
    }

    private List<Promo> createExpectedPromosWtihClones() {
        List<Promo> expectedPromos = createExpectedPromos();
        List<Promo> clonedPromos = createExpectedPromos();

        expectedPromos.addAll(clonedPromos);

        return expectedPromos;
    }

    private List<Promo> createExpectedPromos() {
        Promo firstPromo = PromoBuilder.newPromoBuilder().setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display, PoE and PC Port").
                setDiscount(0.42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126").setClaimPerUnit(9.45).setVersion(8).build();
        Promo secondPromo = PromoBuilder.newPromoBuilder().setPartNumber("SPA112").setDescription("2 Port Phone Adapter").
                setDiscount(0.45).setName("PP-SBFa81137-130126").setGpl(69).setCode("PP-SBFa81137-130126").setClaimPerUnit(5.52).setVersion(8).build();
        Promo thirdPromo = PromoBuilder.newPromoBuilder().setPartNumber("CISCO881-K9").setDescription("Cisco 881 Ethernet Sec Router").
                setDiscount(0.48).setName("PP-Fast70694-120128").setGpl(649).setCode("PP-Fast70694-120128").setClaimPerUnit(38.94).setVersion(12).build();
        return Lists.newArrayList(firstPromo, secondPromo, thirdPromo);
    }
}
