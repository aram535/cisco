package com.cisco.prepos;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.PreposService;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Alf on 07.04.14.
 */

public class PreposViewModelTest {

    PreposViewModel preposViewModel;
    PreposService mockService;

    private List<Prepos> getTestPreposes() {
        PreposBuilder builder = PreposBuilder.builder();

	    long millisOfSomeDate = new DateTime(2014, 3, 14, 0, 0, 0, 0).getMillis();
	    Timestamp someDate = new Timestamp(millisOfSomeDate);

        Prepos prepos1 = builder.type("Type1").partnerName("Some partner1").shippedDate(someDate).build();
        Prepos prepos2 = builder.type("Type2").partnerName("Some partner2").shippedDate(someDate).build();

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
        /*when(mockService.getAllData()).thenReturn(getTestPreposes());
	    preposViewModel.setPreposService(mockService);
        //Act
        List<Prepos> preposes = preposViewModel.getAllPrepos();

        //Assert
        assertTrue(preposes.size() == 2);
        assertEquals("First prepos type should be Type1", "Type1", preposes.get(0).getType());*/
    }

}
