package com.cisco.accountmanager.dao;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.hibernate.BasicDb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 21:34
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateAccountManagerDaoTest extends BasicDb {

    @SpringBeanByType
    private AccountManagerDao accountManagerDao;

    @Test
    @DataSet("account_managers.xml")
    public void thatReturnsManagersFromDb() {
        List<AccountManager> accountManagers = accountManagerDao.getAccountManagers();
        assertThat(accountManagers)
                .isNotNull()
                .hasSize(1)
                .contains(expectedAccountManager());
    }

    private AccountManager expectedAccountManager() {
        return new AccountManager(1, "Manager", "{partners:[]}", "{endUsers:[]}");
    }

}
