package com.cisco.pricelists.service;

import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.dto.PricelistBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.data.MapEntry.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 22.04.2014
 * Time: 23:14
 */
@RunWith(MockitoJUnitRunner.class)
public class PricelistsServiceTest {

    @InjectMocks
    private PricelistsService pricelistsService = new DefaultPricelistsService();

    @Mock
    private PricelistsDao pricelistsDao;

    private static final String FIRST_PART_NUMBER = "First part number";

    private static final Pricelist FIRST_PRICE = PricelistBuilder.newPricelistBuilder().setId(1L).setPartNumber(FIRST_PART_NUMBER).
            setDescription("description").setDiscount(30).setGpl(500d).setWpl(300).build();

    private static final String SECOND_PART_NUMBER = "Second part number";

    private static final Pricelist SECOND_PRICE = PricelistBuilder.newPricelistBuilder().setId(2L).setPartNumber(SECOND_PART_NUMBER).
            setDescription("description").setDiscount(30).setGpl(500d).setWpl(400).build();

    @Test
    public void thatPricelistsMapReturnedAccordingToDao() {
        when(pricelistsDao.getPricelists()).thenReturn(createExpectedPricelists());

        Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();

        assertThat(pricelistsMap).isNotNull().isNotEmpty();
        assertThat(pricelistsMap).hasSize(2).contains(entry(FIRST_PART_NUMBER, FIRST_PRICE), entry(SECOND_PART_NUMBER, SECOND_PRICE));
    }

    @Test
    public void thatPricelistsJustReturnedFromDao() {
        when(pricelistsDao.getPricelists()).thenReturn(createExpectedPricelists());

        List<Pricelist> pricelists = pricelistsService.getPricelists();

        assertThat(pricelists).isNotNull().isNotEmpty();
        assertThat(pricelists).hasSize(2).contains(FIRST_PRICE, SECOND_PRICE);
    }

    @Test
    public void thatPricelistsJustDelegatesSaveAllToDao() {
        List<Pricelist> expectedPricelists = createExpectedPricelists();
        pricelistsService.saveAll(expectedPricelists);

        verify(pricelistsDao).saveAll(expectedPricelists);
    }

    private List<Pricelist> createExpectedPricelists() {
        return Lists.newArrayList(FIRST_PRICE, SECOND_PRICE);
    }
}
