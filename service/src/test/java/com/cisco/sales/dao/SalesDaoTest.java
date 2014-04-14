package com.cisco.sales.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:50
 */
public class SalesDaoTest extends BasicDb {

    @SpringBeanByType
    private SalesDao salesDao;

    @Test
    @DataSet("sales.xml")
    public void thatReturnsAllRecordsFromDb() {
        List<Sale> sales = salesDao.getAll();
        assertThat(sales).isNotEmpty().isEqualTo(createExpectedSales());
    }

    private List<Sale> createExpectedSales() {
        long millisOfShippedDate = new DateTime(2014, 4, 14, 0, 0, 0, 0).getMillis();
        Date shippedDate = new Date(millisOfShippedDate);
        Sale firstSale = SaleBuilder.builder().id(1).shippedDate(shippedDate).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip("61052").partNumber("SPA112").quantity(5).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(Sale.Status.NOT_PROCESSED).build();
        Sale secondSale = SaleBuilder.builder().id(2).shippedDate(shippedDate).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip("61052").partNumber("SPA114").quantity(3).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(Sale.Status.PROCESSED).build();
        return Lists.newArrayList(firstSale, secondSale);
    }

}
