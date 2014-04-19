package com.cisco.darts.dao;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.hibernate.BasicDb;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.sql.Timestamp;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Alf on 15.04.14.
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateDartsDaoTest extends BasicDb {

    @SpringBeanByType
    private DartsDao dartsDao;

    @Test
    @DataSet("darts.xml")
    public void thatGetDartsReturnsAllFromDb() throws Exception {
	    List<Dart> darts = dartsDao.getDarts();

	    assertThat(darts).isNotEmpty();
	    assertThat(darts.size()).isEqualTo(2);
    }

    @Test
    @DataSet("darts.xml")
    @ExpectedDataSet("darts-save-result.xml")
    public void thatSaveAddsDataToDartsDb() throws Exception {
        Dart dart = createNewDart();
        dartsDao.save(dart);
    }

    @Test
    @DataSet("darts.xml")
    @ExpectedDataSet("darts-update-result.xml")
    public void thatUpdateAmendsDataToDartsDb() throws Exception {
        Dart dart = createExpectedDart();
        dart.setCiscoSku("newCiscoSku");
        dartsDao.update(dart);
    }

    @Test
    @DataSet("darts.xml")
    public void thatDeleteRemovesDataToDartsDb() throws Exception {
	    Dart dart = createExpectedDart();
	    dartsDao.delete(dart);

	    assertThat(dartsDao.getDarts().size()).isEqualTo(1);
    }

    private Dart createExpectedDart() {
        long millisOfSomeDate = new DateTime(2014, 4, 14, 0, 0, 0, 0).getMillis();
        Timestamp someDate = new Timestamp(millisOfSomeDate);

        Dart dart = DartBuilder.builder().setId(1).setAuthorizationNumber("MDMF-4526117-1403").setVersion(1)
		        .setDistributorInfo("ERC").setStartDate(someDate).setEndDate(someDate).setDistiDiscount(20.20)
		        .setResellerName("ResName").setResellerCountry("Ukraine").setResellerAcct(123)
		        .setEndUserName("EndUserName").setEndUserCountry("Country").setQuantity(10).setCiscoSku("CiscoSku")
		        .setDistiSku("DistiSku").setListPrice("LPrice").setClaimUnit("Unit").setExtCreditAmt("Amt")
		        .setFastTrackPie("FTpie").setIpNgnPartnerPricingEm("INPPE").setMdmFulfillment("Fullfillment").build();

        return dart;
    }

    private Dart createNewDart() {
        long millisOfSomeDate = new DateTime(2014, 4, 15, 0, 0, 0, 0).getMillis();
        Timestamp someDate = new Timestamp(millisOfSomeDate);

        Dart dart = DartBuilder.builder().setAuthorizationNumber("MDMF-4526117-1403").setVersion(3)
                .setDistributorInfo("ERC").setStartDate(someDate).setEndDate(someDate).setDistiDiscount(20.20)
                .setResellerName("ResName").setResellerCountry("Poland").setResellerAcct(123)
                .setEndUserName("EndUserName").setEndUserCountry("Country").setQuantity(10).setCiscoSku("CiscoSku")
                .setDistiSku("DistiSku").setListPrice("LPrice").setClaimUnit("Unit").setExtCreditAmt("Amt")
                .setFastTrackPie("FTpie").setIpNgnPartnerPricingEm("INPPE").setMdmFulfillment("Fullfillment").build();

        return dart;
    }
}
