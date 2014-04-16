package com.cisco.sales.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.sql.Timestamp;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:50
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SalesDaoTest extends BasicDb {

    @SpringBeanByType
    private SalesDao salesDao;

    @Test
    @DataSet("sales.xml")
    public void thatReturnsAllRecordsFromDb() {
        List<Sale> sales = salesDao.getAll();
        assertThat(sales).isNotEmpty().contains(createExpectedSales());
    }

    private Sale[] createExpectedSales() {

        long millisOfShippedDate = new DateTime(2014, 4, 14, 0, 0, 0, 0).getMillis();
        Timestamp shippedDate = new Timestamp(millisOfShippedDate);

        Sale firstSale = SaleBuilder.builder().id(1).shippedDate(shippedDate).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip("61052").partNumber("SPA112").quantity(5).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(Sale.Status.NOT_PROCESSED).build();

        Sale secondSale = SaleBuilder.builder().id(2).shippedDate(shippedDate).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip("61052").partNumber("SPA114").quantity(3).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(Sale.Status.PROCESSED).build();

        Sale[] sales = new Sale[2];
        sales[0] = firstSale;
        sales[1] = secondSale;
        return sales;
    }

}
