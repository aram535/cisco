package com.cisco.promos.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
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
public class HibernatePromosDaoTest extends BasicDb{

	@SpringBeanByType
	private PromosDao clientsDao;

	@Test
	@DataSet("promos.xml")
	public void thatGetPromosReturnsAllPromosFromDb() {
		List<Promo> promos = clientsDao.getPromos();

		assertThat(promos).isNotEmpty();
		assertThat(promos.size()).isEqualTo(2);
	}

	@Test
	@DataSet("promos.xml")
	@ExpectedDataSet("promos-save-result.xml")
	public void thatSaveAddsDataRowToDb() {
		clientsDao.save(createNewPromo());
	}

	@Test
	@DataSet("promos.xml")
	@ExpectedDataSet("promos-update-result.xml")
	public void thatUpdateAmendsDataInToDb() {
		Promo promo = createExpectedPromo();
		promo.setPartNumber("New Part Number");
		clientsDao.update(promo);
	}

	@Test
	@DataSet("promos.xml")
	public void thatDeleteRemovesFromInToDb() {
		Promo promo = createExpectedPromo();
		clientsDao.delete(promo);

		List<Promo> promos = clientsDao.getPromos();

		assertThat(promos).isNotNull();
		assertThat(promos.size()).isEqualTo(1);
	}

	private Promo createNewPromo() {
		return PromoBuilder.newPromoBuilder().setPartNumber("Some Part Number").setDescription("4 Line IP Phone With Display, PoE and PC Port")
				.setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
				.setClaimPerUnit(9.45).setVersion(8).build();
	}

	private Promo createExpectedPromo() {
		return PromoBuilder.newPromoBuilder().setId(1).setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display, PoE and PC Port")
				.setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
				.setClaimPerUnit(9.45).setVersion(8).build();
	}
}
