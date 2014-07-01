package com.cisco.accountmanager.dao;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.hibernate.BasicDb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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
                .contains(accountManager());
    }

    @Test
    @DataSet("account_managers.xml")
    @ExpectedDataSet("updated_managers.xml")
    public void thatUpdateExistRecordAndSaveNewOne() {
        accountManagerDao.saveOrUpdate(newArrayList(updateAccountManager(), secondAccountManager()));
    }

    private AccountManager accountManager() {
        return new AccountManager(1, "Manager", "{partners:[]}", "{endUsers:[]}");
    }

    private AccountManager updateAccountManager() {
        return new AccountManager(1, "Manager", "{updated partners:[]}", "{updated endUsers:[]}");
    }

    private AccountManager secondAccountManager() {
        AccountManager secondAccountManager = new AccountManager();
        secondAccountManager.setName("Second Manager");
        secondAccountManager.setJsonPartners("{second partners:[]}");
        secondAccountManager.setJsonEndUsers("{second endUsers:[]}");
        return secondAccountManager;
    }

}
