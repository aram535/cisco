package com.cisco.accountmanager.service;

import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 28.06.2014
 * Time: 15:44
 */
public class AccountManagerProviderTest {

    private static final String SECOND_END_USER = "second end user";
    private static final String FIRST_END_USER = "first end user";
    private static final String SECOND_PARTNER = "second partner";
    private static final String FIRST_PARTNER = "first partner";
    private static final String MANAGER_NAME = "Manager";
    private static final String DEFAULT_MANAGER_NAME = "Default manager";
    private static final AccountManagerModel DEFAULT_MANAGER = new AccountManagerModel(-1L, DEFAULT_MANAGER_NAME, Sets.<String>newHashSet(), Sets.<String>newHashSet());
    private static final long MANAGER_ID = 1L;
    private static final String UNKNOWN_NAME = "unknown";
    private final AccountManagerModel accountManagerModel = createAccountManagerModel();


    private DefaultAccountManagerProvider accountManagerProvider;

    @Before
    public void init() {
        AccountManagerService accountManagerService = mock(AccountManagerService.class);
        AccountManagerModel accountManagerModel = this.accountManagerModel;
        when(accountManagerService.getAccountManagerByPartner(FIRST_PARTNER)).thenReturn(accountManagerModel);
        when(accountManagerService.getAccountManagerByPartner(UNKNOWN_NAME)).thenReturn(DEFAULT_MANAGER);
        when(accountManagerService.getAccountManagerByEndUser(FIRST_END_USER)).thenReturn(accountManagerModel);
        when(accountManagerService.getAccountManagerByEndUser(UNKNOWN_NAME)).thenReturn(DEFAULT_MANAGER);
        when(accountManagerService.getDefaultAccountManager()).thenReturn(DEFAULT_MANAGER);
        accountManagerProvider = new DefaultAccountManagerProvider();
        accountManagerProvider.setAccountManagerService(accountManagerService);
    }

    @Test
    public void thatReturnsAccountManagerAccordingToPartnerName() {
        AccountManagerModel accountManager = accountManagerProvider.getAccountManager(FIRST_PARTNER, FIRST_END_USER);
        assertThat(accountManager).isEqualTo(accountManagerModel);
    }

    @Test
    public void thatReturnsAccountManagerAccordingToEndUser() {
        AccountManagerModel accountManager = accountManagerProvider.getAccountManager(UNKNOWN_NAME, FIRST_END_USER);
        assertThat(accountManager).isEqualTo(accountManagerModel);
    }

    @Test
    public void thatReturnsDefaultManagerIfPartnerAndEndUserNotFound() {
        AccountManagerModel accountManager = accountManagerProvider.getAccountManager(UNKNOWN_NAME, UNKNOWN_NAME);
        assertThat(accountManager).isEqualTo(DEFAULT_MANAGER);
    }

    private AccountManagerModel createAccountManagerModel() {
        return new AccountManagerModel(MANAGER_ID, MANAGER_NAME, newHashSet(FIRST_PARTNER, SECOND_PARTNER), newHashSet(FIRST_END_USER, SECOND_END_USER));
    }
}
