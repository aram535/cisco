package com.cisco.prepos;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.PreposService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 07.04.14.
 */

public class PreposViewModelTest {

    PreposViewModel preposViewModel;
    PreposService mockService;

    private List<Prepos> getTestPreposes() {
        PreposBuilder builder = PreposBuilder.builder();
        Prepos prepos1 = builder.type("Type1").partnerName("Some partner1").shippedDate(new Date().getTime()).build();
        Prepos prepos2 = builder.type("Type2").partnerName("Some partner2").shippedDate(new Date().getTime()).build();

        return Lists.newArrayList(prepos1, prepos2);
    }

    @Before
    public void setUp() throws Exception {

        mockService = mock(PreposService.class);
        preposViewModel = new PreposViewModel();
    }

    @Test
    public void getAllPreposReturnsDataCorrectly() throws Exception {
        //Arrange
        when(mockService.getAllData()).thenReturn(getTestPreposes());
	    preposViewModel.setPreposService(mockService);
        //Act
        List<Prepos> preposes = preposViewModel.getAllPrepos();

        //Assert
        assertTrue(preposes.size() == 2);
        assertEquals("First prepos type should be Type1", "Type1", preposes.get(0).getType());
    }

}
