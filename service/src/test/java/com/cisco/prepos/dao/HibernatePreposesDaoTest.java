package com.cisco.prepos.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
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
 * Created by Alf on 23.04.2014.
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernatePreposesDaoTest extends BasicDb {


    @SpringBeanByType
    private PreposesDao preposesDao;

    @Test
    @DataSet("preposes.xml")
    public void thatGetPrepossReturnsAllFromDb() throws Exception {
        List<Prepos> preposes = preposesDao.getPreposes();

        assertThat(preposes).isNotEmpty();
        assertThat(preposes.size()).isEqualTo(2);
    }

    @Test
    @DataSet("preposes.xml")
    @ExpectedDataSet("preposes-save-result.xml")
    public void thatSaveAddsDataToPrepossDb() throws Exception {
        Prepos prepos = createNewPrepos();
        preposesDao.save(prepos);
    }


    @Test
    @DataSet("preposes.xml")
    @ExpectedDataSet("preposes-update-result.xml")
    public void thatUpdateAmendsDataToPrepossDb() throws Exception {
        Prepos prepos = createExpectedPrepos();
        prepos.setType("NEW TYPE");
        preposesDao.update(prepos);
    }


    @Test
    @DataSet("preposes.xml")
    public void thatDeleteRemovesDataToPrepossDb() throws Exception {
        Prepos prepos = createExpectedPrepos();
        preposesDao.delete(prepos);

        assertThat(preposesDao.getPreposes().size()).isEqualTo(1);
    }

    private Prepos createNewPrepos() {

        long millisOfSomeDate = new DateTime(2014, 3, 14, 0, 0, 0, 0).getMillis();
        Timestamp someDate = new Timestamp(millisOfSomeDate);

        Prepos newPrepos = PreposBuilder.builder().type("CISCO MIMIMI").partnerName("SPEZVUZAUTOMATIKA")
                .partNumber("CISCO881-K9").posSum(337.48).quantity(1).ok(true).delta(7).saleDiscount(41)
                .buyDiscount(48).salePrice(383).buyPrice(337.48).firstPromo("PP-FAST70694-120128").secondPromo("")
                .endUser("").clientNumber("158").shippedDate(someDate).shippedBillNumber("1/2606761")
                .comment("КИЕВ 14/03 + DDP АКЦИЯ 8641").serials("SFCZ1805C4AV").zip(61052)
		        .status(Prepos.Status.NOT_POS)
                .build();

        return newPrepos;
    }

    private Prepos createExpectedPrepos() {
        long millisOfSomeDate = new DateTime(2014, 3, 14, 0, 0, 0, 0).getMillis();
        Timestamp someDate = new Timestamp(millisOfSomeDate);

        Prepos newPrepos = PreposBuilder.builder().id(1).type("CISCO SB").partnerName("SPEZVUZAUTOMATIKA")
                .partNumber("CISCO881-K9").posSum(337.48).quantity(1).ok(true).delta(7).saleDiscount(41)
                .buyDiscount(48).salePrice(383).buyPrice(337.48).firstPromo("PP-FAST70694-120128").secondPromo("")
                .endUser("").clientNumber("158").shippedDate(someDate).shippedBillNumber("1/2606761")
                .comment("КИЕВ 14/03 + DDP АКЦИЯ 8641").serials("SFCZ1805C4AV").zip(61052)
		        .status(Prepos.Status.NOT_POS)
                .build();

        return newPrepos;
    }

}
