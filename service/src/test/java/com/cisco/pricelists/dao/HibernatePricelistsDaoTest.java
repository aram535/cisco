package com.cisco.pricelists.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.pricelists.dto.Pricelist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Alf on 19.04.2014.
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernatePricelistsDaoTest extends BasicDb {

	@SpringBeanByType
	private PricelistsDao pricelistsDao;

	@Test
	@DataSet("pricelists.xml")
	public void thatGetPricelistsReturnsAllFromDb() {
		List<Pricelist> pricelists = pricelistsDao.getPricelists();

		assertThat(pricelists).isNotEmpty();
		assertThat(pricelists.size()).isEqualTo(2);
	}

	@Test
	@DataSet("pricelists.xml")
	@ExpectedDataSet("pricelists-save-result.xml")
	public void thatSaveAddsDataToDb() {
		Pricelist pricelist = createNewPricelist();
		pricelistsDao.save(pricelist);
	}

	@Test
	@DataSet("pricelists.xml")
	@ExpectedDataSet("pricelists-update-result.xml")
	public void thatUpdateAmmendsDataInDb() throws Exception {
		Pricelist pricelist = createExpectedPricelist();
		pricelist.setPartNumber("New Part Number");
		pricelistsDao.update(pricelist);
	}

	@Test
	@DataSet("pricelists.xml")
	public void thatDeleteRemovesDataFromDB() throws Exception {
		Pricelist pricelist = createExpectedPricelist();
		pricelistsDao.delete(pricelist);

		assertThat(pricelistsDao.getPricelists().size()).isEqualTo(1);
	}

	private Pricelist createExpectedPricelist() {
		Pricelist pricelist = new Pricelist();
		pricelist.setId(1L);
		pricelist.setPartNumber("SPA112");
		pricelist.setDescription("2 Port Phone Adapter");
		pricelist.setGpl(69);
		pricelist.setWpl(43.47);
		pricelist.setDiscount(37);
		return pricelist;
	}

	private Pricelist createNewPricelist() {
		Pricelist pricelist = new Pricelist();
		pricelist.setPartNumber("SPA114");
		pricelist.setDescription("4 Port Phone Adapter");
		pricelist.setGpl(60);
		pricelist.setWpl(43.47);
		pricelist.setDiscount(37);
		return pricelist;
	}
}
