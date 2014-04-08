package com.cisco.ui.names;

import com.cisco.names.dto.Names;
import com.cisco.names.service.DefaultNamesService;
import com.cisco.names.service.NamesService;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alf on 07.04.14.
 */

public class NamesViewModelTest {

    NamesViewModel namesViewModel = new NamesViewModel();

	private List<Names> getTestNamesData() {
		Names name1 = new Names("1", "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
		Names name2 = new Names("2", "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");
		Names name3 = new Names("3", "333", "SPEZVUZAUTOMATIKA", "ODESSA", "str. Dyuka 3a");

		return Lists.newArrayList(name1, name2, name3);
	}

	@Test
	public void getAllDataReturnsDataCorrectly() throws Exception {

		//Arrange
		NamesService mockService = mock(DefaultNamesService.class);
		when(mockService.getAllData()).thenReturn(getTestNamesData());
		namesViewModel.setNamesService(mockService);
		//Act
		List<Names> resultData = namesViewModel.getAllNames();

		//Assert
		assertNotNull("getAllData() result should not be null", resultData);
		assertEquals(3, resultData.size());
		assertEquals("KHARKOV", resultData.get(0).getCity());
	}

}
