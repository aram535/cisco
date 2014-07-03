package com.cisco.accountmanager.service;

import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.collect.Sets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.cisco.accountmanager.service.DefaultAccountManagerService.DEFAULT_MANAGER;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.*;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 28.06.2014
 * Time: 15:44
 */
@RunWith(JUnitParamsRunner.class)
public class AccountManagerProviderTest {

    private static final String SECOND_END_USER = "second end user";
    private static final String FIRST_END_USER = "first end user";
    private static final String SECOND_PARTNER = "second partner";
    private static final String FIRST_PARTNER = "first partner";
    private static final String MANAGER_NAME = "Manager";
    private static final long MANAGER_ID = 1L;
    private static final String UNKNOWN_NAME = "unknown";


    private DefaultAccountManagerProvider accountManagerProvider;

    @Before
    public void init() {
        AccountManagerService accountManagerService = mock(AccountManagerService.class);
        AccountManagerModel accountManagerModel = createAccountManagerModel();
        when(accountManagerService.getAccountManagerByPartner(FIRST_PARTNER)).thenReturn(accountManagerModel);
        when(accountManagerService.getAccountManagerByPartner(UNKNOWN_NAME)).thenReturn(DEFAULT_MANAGER);
        when(accountManagerService.getAccountManagerByEndUser(FIRST_END_USER)).thenReturn(accountManagerModel);
        when(accountManagerService.getAccountManagerByEndUser(UNKNOWN_NAME)).thenReturn(DEFAULT_MANAGER);

        accountManagerProvider = new DefaultAccountManagerProvider();
        accountManagerProvider.setAccountManagerService(accountManagerService);
    }

    @Test
    @Parameters(method = "parameters")
    public void thatReturnsAccountManagerAccordingToRules(String partnerName, String endUserName, AccountManagerModel result) {
        AccountManagerModel accountManager = accountManagerProvider.getAccountManager(partnerName, endUserName);
        assertThat(accountManager).isEqualTo(result);
    }

    private Object[] parameters() {
        return $($(FIRST_PARTNER, FIRST_END_USER, createAccountManagerModel()),
                $(UNKNOWN_NAME, FIRST_END_USER, createAccountManagerModel()),
                $(UNKNOWN_NAME, UNKNOWN_NAME, DEFAULT_MANAGER));
    }

    private AccountManagerModel createAccountManagerModel() {
        return new AccountManagerModel(MANAGER_ID, MANAGER_NAME, newHashSet(FIRST_PARTNER, SECOND_PARTNER), newHashSet(FIRST_END_USER, SECOND_END_USER));
    }
}
