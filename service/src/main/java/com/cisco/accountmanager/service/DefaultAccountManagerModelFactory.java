package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 23:30
 */
@Component
public class DefaultAccountManagerModelFactory implements AccountManagerModelFactory {

    @Autowired
    private JsonConverter jsonConverter;

    @Override
    public List<AccountManagerModel> createModels(List<AccountManager> accountManagers) {

        List<AccountManagerModel> accountManagerModels = newArrayList(transform(accountManagers, new Function<AccountManager, AccountManagerModel>() {
            @Override
            public AccountManagerModel apply(AccountManager accountManager) {
                String jsonPartners = accountManager.getJsonPartners();
                List<String> partners = jsonConverter.fromJson(jsonPartners);

                String jsonEndUsers = accountManager.getJsonEndUsers();
                List<String> endUsers = jsonConverter.fromJson(jsonEndUsers);
                return new AccountManagerModel(accountManager.getId(), accountManager.getName(), partners, endUsers);
            }
        }));

        return accountManagerModels;
    }
}
