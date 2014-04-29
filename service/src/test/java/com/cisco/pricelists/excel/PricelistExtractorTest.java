package com.cisco.pricelists.excel;

import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:24
 */
public class PricelistExtractorTest {

    private PricelistExtractor pricelistExtractor = new DefaultPricelistExtractor();

    @Test
    public void thatExtractReturnsAllDataFromExcelFile() throws URISyntaxException {
        URL pricelistUrl = this.getClass().getResource("/templates/Pricelist.xlsx");
        File pricelistFile = new File(pricelistUrl.toURI());

        List<Pricelist> extractedPrices = pricelistExtractor.extract(pricelistFile);

        assertThat(extractedPrices).isNotNull().isNotEmpty().hasSize(4);
        assertThat(extractedPrices).containsAll(createExpectedPricelist());
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
