package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.cisco.pricelists.service.PricelistsService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 18:36
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPricelistImporterTest {

    @InjectMocks
    private PricelistImporter pricelistImporter = new DefaultPricelistImporter();

    @Mock
    private PricelistExtractor pricelistExtractor;

    @Mock
    private InputStream inputStream;

    @Mock
    private PricelistsService pricelistsService;


    @Before
    public void init() {
        when(pricelistExtractor.extract(inputStream)).thenReturn(createExpectedPricelist());
    }

    @Test(expected = CiscoException.class)
    public void thatImportPricelistsThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {
        when(pricelistExtractor.extract(inputStream)).thenReturn(null);
        pricelistImporter.importPricelist(inputStream);
    }

    @Test
    public void thatImportPricelistsJustReplacedAllData() {

        pricelistImporter.importPricelist(inputStream);

        verify(pricelistExtractor).extract(inputStream);
        verify(pricelistsService).deleteAll();
        verify(pricelistsService).saveAll(Lists.newArrayList(createExpectedPricelist().values()));

        verifyNoMoreInteractions(pricelistExtractor, pricelistsService);
    }

    @Test
    public void thatImportedPricelistsContainNoClones() {
        when(pricelistExtractor.extract(inputStream)).thenReturn(createExpectedPricelistWtihClones());
        pricelistImporter.importPricelist(inputStream);

        verify(pricelistsService).saveAll(Lists.newArrayList(createExpectedPricelist().values()));
    }

    private Map<String, Pricelist> createExpectedPricelistWtihClones() {

        Map<String, Pricelist> pricelistMap = createExpectedPricelist();

        Map<String, Pricelist> clonedPricelistMap = Maps.newLinkedHashMap();
        clonedPricelistMap.putAll(pricelistMap);

        Pricelist firstPricelistClone = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA112").setDescription("2 Port Phone Adapter")
                .setWpl(43.47).setGpl(69d).setDiscount(37).build();

        clonedPricelistMap.put(firstPricelistClone.getPartNumber(), firstPricelistClone);

        return clonedPricelistMap;
    }

    private Map<String, Pricelist> createExpectedPricelist() {

        Pricelist firstPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA112").setDescription("2 Port Phone Adapter")
                .setWpl(43.47).setGpl(69d).setDiscount(37).build();

        Pricelist secondPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SF100D-08-EU").setDescription("SF100D-08 8-Port 10/100 Desktop Switch")
                .setWpl(35.91).setGpl(57d).setDiscount(37).build();

        Pricelist thirdPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display PoE and PC Port")
                .setWpl(119.07).setGpl(189d).setDiscount(37).build();

        Pricelist fourthPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("CISCO881-K9").setDescription("Cisco 881 Ethernet Sec Router")
                .setWpl(376.42).setGpl(649d).setDiscount(42).build();

        return ImmutableMap.of(firstPrice.getPartNumber(), firstPrice,
                secondPrice.getPartNumber(), secondPrice,
                thirdPrice.getPartNumber(), thirdPrice,
                fourthPrice.getPartNumber(), fourthPrice);
    }


}
