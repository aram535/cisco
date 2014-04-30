package com.cisco.promos.excel;

import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 0:16
 */
public class PromosExtractorTest {

    private PromosExtractor promosExtractor = new DefaultPromosExtractor();

    @Test
    public void thatExtractReturnsAllDataFromExcelFile() throws URISyntaxException {
        InputStream inputStream = this.getClass().getResourceAsStream("/templates/Promos.xlsx");


        List<Promo> extractedPrices = promosExtractor.extract(inputStream);

        assertThat(extractedPrices).isNotNull().isNotEmpty().hasSize(3);
        assertThat(extractedPrices).containsAll(createExpectedPromos());
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
