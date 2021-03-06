package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 23:30
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountManagerModelFactoryTest {

    private static final String PARTNERS_JSON = "partners";
    private static final String FIRST_PARTNER = "first partner";
    private static final String SECOND_PARTNER = "second partner";
    private static final String END_USERS_JSON = "end users json";
    private static final String FIRST_END_USER = "first end user";
    private static final String SECOND_END_USER = "second end user";
    private static final long MANAGER_ID = 1L;
    private static final String MANAGER_NAME = "Manager";

    @Mock
    private JsonConverter jsonConverter;

    @InjectMocks
    private AccountManagerModelFactory accountManagerModelFactory = new DefaultAccountManagerModelFactory();

    @Before
    public void init() {
        when(jsonConverter.fromJson(PARTNERS_JSON)).thenReturn(getPartners());
        when(jsonConverter.fromJson(END_USERS_JSON)).thenReturn(getEndUsers());
        when(jsonConverter.toJson(getPartners())).thenReturn(PARTNERS_JSON);
        when(jsonConverter.toJson(getEndUsers())).thenReturn(END_USERS_JSON);
    }

    @Test
    public void thatCreatesEmptyModelsListIfInputIsNull() {
        List<AccountManagerModel> models = accountManagerModelFactory.createModels(null);
        assertThat(models).isNotNull().isEmpty();
    }

    @Test
    public void thatCreatesModelsFromInputManagersList() {
        List<AccountManagerModel> accountManagerModels = accountManagerModelFactory.createModels(createManagersList());
        assertThat(accountManagerModels)
                .isNotNull()
                .hasSize(1)
                .isEqualTo(createModelsList());
    }

    @Test
    public void thatCreatesEmptyManagersListIfInputIsNull() {
        List<AccountManager> managers = accountManagerModelFactory.createManagers(null);
        assertThat(managers).isNotNull().isEmpty();
    }

    @Test
    public void thatCreatesManagersFromInputModelsList() {
        List<AccountManager> accountManagers = accountManagerModelFactory.createManagers(createModelsList());
        assertThat(accountManagers)
                .isNotNull()
                .hasSize(1)
                .isEqualTo(createManagersList());
    }

    private Set<String> getPartners() {
        return newHashSet(FIRST_PARTNER, SECOND_PARTNER);
    }

    private Set<String> getEndUsers() {
        return newHashSet(FIRST_END_USER, SECOND_END_USER);
    }

    private List<AccountManager> createManagersList() {
        return newArrayList(new AccountManager(MANAGER_ID, MANAGER_NAME, PARTNERS_JSON, END_USERS_JSON));
    }

    private List<AccountManagerModel> createModelsList() {
        return newArrayList(new AccountManagerModel(MANAGER_ID, MANAGER_NAME, getPartners(), getEndUsers()));
    }
}
