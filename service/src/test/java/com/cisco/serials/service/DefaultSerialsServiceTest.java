package com.cisco.serials.service;

import com.cisco.exception.CiscoException;
import com.cisco.serials.dao.SerialsDao;
import com.cisco.serials.dto.Serial;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSerialsServiceTest {

	@InjectMocks
	SerialsService serialsService  = new DefaultSerialsService();

	@Mock
	SerialsDao serialsDao;

	@Test(expected = CiscoException.class)
	public void thatExceptionIsThrownWhenInputListIsEmpty() throws Exception {

		serialsService.saveOrUpdate(Lists.<Serial>newArrayList());
	}

	@Test
	public void thatSerialAreUpdatedSuccessfully() throws Exception {

		List<Serial> serials = Lists.newArrayList(new Serial("ABC123456"));

		serialsService.saveOrUpdate(serials);

		verify(serialsDao, times(1)).saveOrUpdate(serials);
		verifyNoMoreInteractions(serialsDao);
	}
}