package com.cisco.serials.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.serials.dto.Serial;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateSerialsDaoTest  extends BasicDb {

	@SpringBeanByType
	private SerialsDao serialsDao;

	@Test
	@DataSet("serials.xml")
	@ExpectedDataSet("serials-saveOrUpdate-result.xml")
	public void thatNewSerialsSuccessfullySaved() throws Exception {

		List<Serial> serials = newSerialsList();

		serialsDao.saveOrUpdate(serials);
	}

	private List<Serial> newSerialsList() {

		Serial firstSerial = new Serial("ABC123456");
		Serial secondSerial = new Serial("EFG123456");

		return Lists.newArrayList(firstSerial, secondSerial);
	}
}