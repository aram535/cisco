package com.cisco.sales.service;

import com.cisco.sales.dao.SalesDao;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.dto.SaleBuilder;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.List;

import static com.cisco.sales.dto.Sale.Status.NEW;
import static com.cisco.sales.dto.Sale.Status.OLD;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 15.04.2014
 * Time: 23:55
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesServiceTest {

    private static final Timestamp CURRENT_TIME = new Timestamp(DateTime.now().getMillis());

    private Sale newSale;

    private Sale oldSale;

    @InjectMocks
    private SalesService salesService = new DefaultSalesService();

    @Mock
    private SalesDao salesDao;

    @Before
    public void init() {
        initExpectedSalesListInDb();
        when(salesDao.getSales()).thenReturn(Lists.newArrayList(newSale, oldSale));
    }

    @Test
    public void thatReturnsOnlySuitableSales() {
        List<Sale> sales = salesService.getSales(NEW);
        assertThat(sales).isNotNull().isNotEmpty();
        assertThat(sales).isEqualTo(Lists.newArrayList(newSale));
    }

    @Test
    public void thatReturnAllSalesIfNoArgumentsSet() {
        List<Sale> sales = salesService.getSales();
        assertThat(sales).isNotNull().isNotEmpty();
        assertThat(sales).hasSize(2);
        assertThat(sales).contains(newSale, oldSale);
    }

    @Test
    public void thatReturnAllSalesIfNullIsInput() {
        List<Sale> sales = salesService.getSales(null);
        assertThat(sales).isNotNull().isNotEmpty();
        assertThat(sales).hasSize(2);
        assertThat(sales).contains(newSale, oldSale);
    }

    @Test
    public void thatReturnsEmptyListIfNoOccurrencesWithInputArguments() {
        when(salesDao.getSales()).thenReturn(Lists.newArrayList(newSale));
        List<Sale> sales = salesService.getSales(OLD);
        assertThat(sales).isNotNull().isEmpty();
    }

    private void initExpectedSalesListInDb() {

        newSale = SaleBuilder.builder().id(1).shippedDate(CURRENT_TIME).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip(61052).partNumber("SPA112").quantity(5).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(NEW).build();

        oldSale = SaleBuilder.builder().id(2).shippedDate(CURRENT_TIME).shippedBillNumber("1267894").
                clientName("Spec").clientNumber("158").clientZip(61052).partNumber("SPA114").quantity(3).
                serials("ASDFEFE321321").price(20.83).ciscoType("CISCO SB").comment("comment").status(OLD).build();


    }

}
