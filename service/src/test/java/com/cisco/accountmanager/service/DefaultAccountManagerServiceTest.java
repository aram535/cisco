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

import static com.cisco.accountmanager.service.DefaultAccountManagerService.DEFAULT_MANAGER;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    }

    @Test
    public void thatReturnsModelsAccordingToDaoOutput() {
        initNotNullManagers();

        List<AccountManagerModel> accountManagers = accountManagerService.getAccountManagers();
        assertThat(accountManagers)
                .isNotNull()
                .hasSize(1)
                .contains(createAccountManagerModel());
    }

    @Test
    public void thatReturnsDefaultManagerIfNoOccurrenceByPartnerName() {
        initNotNullManagers();

        AccountManagerModel manager = accountManagerService.getAccountManagerByPartner("unknown partner");
        assertThat(manager).isEqualTo(DEFAULT_MANAGER);
    }

    @Test
    public void thatReturnsManagerIfPartnerNameIsInHisList() {
        initNotNullManagers();

        AccountManagerModel manager = accountManagerService.getAccountManagerByPartner(FIRST_PARTNER);
        assertThat(manager).isEqualTo(createAccountManagerModel());
    }

    @Test
    public void thatReturnsDefaultManagerIfNoOccurrenceByEndUserName() {
        initNotNullManagers();

        AccountManagerModel manager = accountManagerService.getAccountManagerByEndUser("unknown end user");
        assertThat(manager).isEqualTo(DEFAULT_MANAGER);
    }

    @Test
    public void thatReturnsManagerIfEndUserNameIsInHisList() {
        initNotNullManagers();

        AccountManagerModel manager = accountManagerService.getAccountManagerByEndUser(FIRST_END_USER);
        assertThat(manager).isEqualTo(createAccountManagerModel());
    }

    @Test
    public void thatDelegatesSaveOrUpdateToDaoAfterFactoryCreation() {
        List<AccountManagerModel> accountManagersModels = getAccountManagersModels();
        List<AccountManager> accountManagers = getAccountManagers();
        when(accountManagerModelFactory.createManagers(accountManagersModels)).thenReturn(accountManagers);

        accountManagerService.saveOrUpdate(accountManagersModels);

        verify(accountManagerDao).saveOrUpdate(accountManagers);
    }

    @Test
    public void thatDelegatesDeletingToDaoAfterFactoryCreation() {
        List<AccountManagerModel> accountManagersModels = getAccountManagersModels();
        List<AccountManager> accountManagers = getAccountManagers();
        when(accountManagerModelFactory.createManagers(accountManagersModels)).thenReturn(accountManagers);

        accountManagerService.delete(createAccountManagerModel());

        verify(accountManagerDao).delete(createAccountManager());
    }

    private void initNotNullManagers() {
        when(accountManagerDao.getAccountManagers())
                .thenReturn(getAccountManagers());
        when(accountManagerModelFactory.createModels(getAccountManagers()))
                .thenReturn(getAccountManagersModels());
        accountManagerService.init();
    }

    private List<AccountManagerModel> getAccountManagersModels() {
        return newArrayList(createAccountManagerModel());
    }

    private List<AccountManager> getAccountManagers() {
        return newArrayList(createAccountManager());
    }

    private AccountManagerModel createAccountManagerModel() {
        return new AccountManagerModel(MANAGER_ID, MANAGER_NAME, newHashSet(FIRST_PARTNER, SECOND_PARTNER), newHashSet(FIRST_END_USER, SECOND_END_USER));
    }

    private AccountManager createAccountManager() {
        return new AccountManager(MANAGER_ID, MANAGER_NAME, jsonPartners, jsonEndUsers);
    }
}
