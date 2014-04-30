package com.cisco.darts.excel;

import com.cisco.darts.dto.Dart;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

import static com.cisco.darts.dto.DartBuilder.builder;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 28.04.2014
 * Time: 22:52
 */

public class DartsExtractorTest {

    private DartsExtractor dartsExtractor = new DefaultDartsExtractor();

    @Test
    public void thatDartsExtractedFromCorrectFile() throws URISyntaxException {
        InputStream inputStream = this.getClass().getResourceAsStream("/templates/Darts.xlsx");

        List<Dart> extractedDarts = dartsExtractor.extract(inputStream);

        assertThat(extractedDarts).isNotNull().isNotEmpty().hasSize(8);
        assertThat(extractedDarts).containsAll(createExpectedDarts());
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
                .setListPrice(100).setClaimUnit(15).setExtCreditAmt(15)
                .setFastTrackPie(0).setIpNgnPartnerPricingEm(0).setMdmFulfillment(15.00).build();

        Dart secondDart = builder(firstDart).setQuantity(4).setCiscoSku("EHWIC-1GE-SFP-CU=").setListPrice(799).
                setClaimUnit(119.85).setExtCreditAmt(479.4).setFastTrackPie(0).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(119.85).build();

        Dart thirdDart = builder(firstDart).setQuantity(4).setCiscoSku("EHWIC-1GE-SFP-CU").setListPrice(799).
                setClaimUnit(119.85).setExtCreditAmt(479.4).setFastTrackPie(87.89).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(31.96).build();

        Dart fourthDart = builder(firstDart).setQuantity(1).setCiscoSku("CAB-CONSOLE-RJ45=").setListPrice(30).
                setClaimUnit(119.85).setExtCreditAmt(479.4).setFastTrackPie(0).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(31.96).build();

        Dart fifthDart = builder(firstDart).setQuantity(1).setCiscoSku("CAB-SS-V35MT=").setListPrice(100).
                setClaimUnit(15).setExtCreditAmt(15).setFastTrackPie(0).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(15.00).build();

        Dart sixsDart = builder(firstDart).setQuantity(8).setCiscoSku("GLC-SX-MMD=").setListPrice(500).
                setClaimUnit(75).setExtCreditAmt(600).setFastTrackPie(0).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(75.00).build();

        Dart sevensDart = builder(firstDart).setQuantity(1).setCiscoSku("CISCO2951/K9").setListPrice(7500).
                setClaimUnit(1125).setExtCreditAmt(1125).setFastTrackPie(825).setIpNgnPartnerPricingEm(0).
                setMdmFulfillment(300.00).build();

        Dart eightsDart = builder(firstDart).setQuantity(8).setCiscoSku("GLC-T=").setListPrice(395).
                setClaimUnit(59.25).setExtCreditAmt(474).setFastTrackPie(0).setIpNgnPartnerPricingEm(31.6).
                setMdmFulfillment(27.65).build();

        return Lists.newArrayList(firstDart, secondDart, thirdDart, fourthDart, fifthDart, sixsDart, sevensDart, eightsDart);
    }

}
