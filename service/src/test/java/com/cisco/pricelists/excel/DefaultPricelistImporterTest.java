package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
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
    private PricelistsDao pricelistsDao;


    @Before
    public void init() {
        when(pricelistExtractor.extract(inputStream)).thenReturn(createExpectedPricelist());
    }

    @Test(expected = CiscoException.class)
    public void thatImportPromosThrowsCiscoExceptionIfExportedDataIsEmptyOrNull() {
        when(pricelistExtractor.extract(inputStream)).thenReturn(null);
        pricelistImporter.importPricelist(inputStream);
    }

    @Test
    public void thatImportPromosJustReplacedAllData() {
        when(pricelistsDao.deleteAll()).thenReturn(2);
        pricelistImporter.importPricelist(inputStream);

        verify(pricelistExtractor).extract(inputStream);
        verify(pricelistsDao).deleteAll();
        verify(pricelistsDao).saveAll(createExpectedPricelist());
        verifyNoMoreInteractions(pricelistExtractor, pricelistsDao);
    }

    private List<Pricelist> createExpectedPricelist() {

        Pricelist firstPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA112").setDescription("2 Port Phone Adapter")
                .setWpl(43.47).setGpl(69).setDiscount(37).build();

        Pricelist secondPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SF100D-08-EU").setDescription("SF100D-08 8-Port 10/100 Desktop Switch")
                .setWpl(35.91).setGpl(57).setDiscount(37).build();

        Pricelist thirdPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display PoE and PC Port")
                .setWpl(119.07).setGpl(189).setDiscount(37).build();

        Pricelist fourthPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("CISCO881-K9").setDescription("Cisco 881 Ethernet Sec Router")
                .setWpl(376.42).setGpl(649).setDiscount(42).build();

        return Lists.newArrayList(firstPrice, secondPrice, thirdPrice, fourthPrice);
    }


}
