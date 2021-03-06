package com.cisco.pricelists.excel;

import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.google.common.collect.ImmutableMap;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 07.07.2014
 * Time: 20:02
 */
@Ignore
public class BasePricelistExtractor {

    PricelistExtractor pricelistExtractor;

    @Test
    public void thatExtractReturnsAllDataFromExcelFile() throws URISyntaxException {
        InputStream inputStream = this.getClass().getResourceAsStream("/templates/Pricelist.xlsx");

        Map<String, Pricelist> extractedPrices = pricelistExtractor.extract(inputStream);

        assertThat(extractedPrices).isNotNull().isNotEmpty().hasSize(4);
        assertThat(extractedPrices).isEqualTo(createExpectedPricelist());
    }

    @Test
    public void thatExtractReturnsAllDataFromBigExcelFile() throws URISyntaxException {
        InputStream inputStream = this.getClass().getResourceAsStream("/templates/price_ERROR.xlsx");

        Map<String, Pricelist> extractedPrices = pricelistExtractor.extract(inputStream);

        assertThat(extractedPrices).isNotNull().isNotEmpty().hasSize(2965);
    }

    private Map<String, Pricelist> createExpectedPricelist() {

        Pricelist firstPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA112").setDescription("2 Port Phone Adapter")
                .setWpl(43.47).setGpl(69d).setDiscount(0.37).build();

        Pricelist secondPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SF100D-08-EU").setDescription("SF100D-08 8-Port 10/100 Desktop Switch")
                .setWpl(35.91).setGpl(57d).setDiscount(0.37).build();

        Pricelist thirdPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display PoE and PC Port")
                .setWpl(119.07).setGpl(189d).setDiscount(0.37).build();

        Pricelist fourthPrice = PricelistBuilder.newPricelistBuilder().setPartNumber("CISCO881-K9").setDescription("Cisco 881 Ethernet Sec Router")
                .setWpl(376.42).setGpl(649d).setDiscount(0.42).build();

        return ImmutableMap.of(firstPrice.getPartNumber(), firstPrice,
                secondPrice.getPartNumber(), secondPrice,
                thirdPrice.getPartNumber(), thirdPrice,
                fourthPrice.getPartNumber(), fourthPrice);
    }
}
