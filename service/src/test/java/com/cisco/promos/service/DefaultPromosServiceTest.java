package com.cisco.promos.service;

import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * User: Rost
 * Date: 19.08.2014
 * Time: 21:51
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPromosServiceTest {

    @InjectMocks
    private PromosService promosService = new DefaultPromosService();

    @Mock
    private PromosDao promosDao;

    @Test
    public void thatServiceJustDelegatesSaveAllToDao() {
        List<Promo> promos = newArrayList(newPromo());

        promosService.saveAll(promos);

        verify(promosDao).saveAll(promos);
    }
}
