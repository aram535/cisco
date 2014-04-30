package com.cisco.promos.dao;

import com.cisco.hibernate.BasicDb;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.dto.PromoBuilder;
import com.google.common.collect.Lists;
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
public class HibernatePromosDaoTest extends BasicDb {

    @SpringBeanByType
    private PromosDao promosDao;

    @Test
    @DataSet("promos.xml")
    public void thatGetPromosReturnsAllPromosFromDb() {
        List<Promo> promos = promosDao.getPromos();

        assertThat(promos).isNotEmpty();
        assertThat(promos.size()).isEqualTo(2);
    }

    @Test
    @DataSet("promos.xml")
    @ExpectedDataSet("promos-save-result.xml")
    public void thatSaveAddsDataRowToDb() {
        promosDao.save(createNewPromo());
    }

    @Test
    @DataSet("promos.xml")
    @ExpectedDataSet("promos-save-all-result.xml")
    public void thatSaveAddsAllDataRowToDb() {
        promosDao.saveAll(createNewPromos());
    }

    @Test
    @DataSet("promos.xml")
    @ExpectedDataSet("promos-update-result.xml")
    public void thatUpdateAmendsDataInToDb() {
        Promo promo = createExpectedPromo();
        promo.setPartNumber("New Part Number");
        promosDao.update(promo);
    }

    @Test
    @DataSet("promos.xml")
    public void thatDeleteRemovesFromInToDb() {
        Promo promo = createExpectedPromo();
        promosDao.delete(promo);

        List<Promo> promos = promosDao.getPromos();

        assertThat(promos).isNotNull();
        assertThat(promos.size()).isEqualTo(1);
    }

    @Test
    @DataSet("promos.xml")
    public void thatDeleteAllRemovesAllFromDb() {
        int result = promosDao.deleteAll();

        List<Promo> promos = promosDao.getPromos();

        assertThat(result).isEqualTo(2);
        assertThat(promos).isNotNull().isEmpty();
    }

    private Promo createNewPromo() {
        return PromoBuilder.newPromoBuilder().setPartNumber("Some Part Number").setDescription("4 Line IP Phone With Display, PoE and PC Port")
                .setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
                .setClaimPerUnit(9.45).setVersion(8).build();
    }

    private List<Promo> createNewPromos() {
        Promo firstPromo = PromoBuilder.newPromoBuilder().setPartNumber("Some Part Number").setDescription("4 Line IP Phone With Display, PoE and PC Port")
                .setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
                .setClaimPerUnit(9.45).setVersion(8).build();
        Promo secondPromo = PromoBuilder.newPromoBuilder().setPartNumber("Some Other Part Number").setDescription("4 Line IP Phone With Display, PoE and PC Port")
                .setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
                .setClaimPerUnit(9.45).setVersion(8).build();

        return Lists.newArrayList(firstPromo, secondPromo);
    }

    private Promo createExpectedPromo() {
        return PromoBuilder.newPromoBuilder().setId(1).setPartNumber("SPA504G").setDescription("4 Line IP Phone With Display, PoE and PC Port")
                .setDiscount(42).setName("PP-SBFa81137-130126").setGpl(189).setCode("PP-SBFa81137-130126")
                .setClaimPerUnit(9.45).setVersion(8).build();
    }
}
