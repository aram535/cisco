package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dao.AccountManagerDao;
import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 22:46
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountManagerServiceTest {

    private static final String SECOND_END_USER = "second end user";
    private static final String FIRST_END_USER = "first end user";
    private static final String SECOND_PARTNER = "second partner";
    private static final String FIRST_PARTNER = "first partner";
    private static final String MANAGER_NAME = "Manager";
    private static final long MANAGER_ID = 1L;
    private final String jsonPartners = "partners";
    private final String jsonEndUsers = "endUsers";

    @InjectMocks
    private DefaultAccountManagerService accountManagerService = new DefaultAccountManagerService();

    @Mock
    private AccountManagerDao accountManagerDao;

    @Mock
    private AccountManagerModelFactory accountManagerModelFactory;

    @Test
    public void thatReturnsEmptyListOfModelsIfDaoReturnsNull() {
        when(accountManagerDao.getAccountManagers()).thenReturn(null);
        accountManagerService.init();

        List<AccountManagerModel> accountManagers = accountManagerService.getAccountManagers();

        assertThat(accountManagers)
                .isNotNull()
                .isEmpty();
        verifyNoMoreInteractions(accountManagerModelFactory);
    }

    @Test
    public void thatReturnsModelsAccordingToDaoOutput() {
        when(accountManagerDao.getAccountManagers())
                .thenReturn(getAccountManagers());
        when(accountManagerModelFactory.createModels(getAccountManagers()))
                .thenReturn(getAccountManagersModels());
        accountManagerService.init();

        List<AccountManagerModel> accountManagers = accountManagerService.getAccountManagers();
        assertThat(accountManagers)
                .isNotNull()
                .hasSize(1)
                .contains(createAccountManagerModel());
    }

    private List<AccountManagerModel> getAccountManagersModels() {
        return newArrayList(createAccountManagerModel());
    }

    private List<AccountManager> getAccountManagers() {
        return newArrayList(createAccountManager());
    }

    private AccountManagerModel createAccountManagerModel() {
        return new AccountManagerModel(MANAGER_ID, MANAGER_NAME, newArrayList(FIRST_PARTNER, SECOND_PARTNER), newArrayList(FIRST_END_USER, SECOND_END_USER));
    }

    private AccountManager createAccountManager() {
        return new AccountManager(MANAGER_ID, MANAGER_NAME, jsonPartners, jsonEndUsers);
    }
}
