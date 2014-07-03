package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

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

        if (CollectionUtils.isEmpty(accountManagers)) {
            return newArrayList();
        }

        List<AccountManagerModel> accountManagerModels = newArrayList(transform(accountManagers, new Function<AccountManager, AccountManagerModel>() {
            @Override
            public AccountManagerModel apply(AccountManager accountManager) {
                String jsonPartners = accountManager.getJsonPartners();
                Set<String> partners = jsonConverter.fromJson(jsonPartners);

                String jsonEndUsers = accountManager.getJsonEndUsers();
                Set<String> endUsers = jsonConverter.fromJson(jsonEndUsers);
                return new AccountManagerModel(accountManager.getId(), accountManager.getName(), partners, endUsers);
            }
        }));

        return accountManagerModels;
    }

    @Override
    public List<AccountManager> createManagers(List<AccountManagerModel> accountManagerModels) {

        if (CollectionUtils.isEmpty(accountManagerModels)) {
            return newArrayList();
        }

        List<AccountManager> accountManagers = newArrayList(transform(accountManagerModels, new Function<AccountManagerModel, AccountManager>() {

            @Override
            public AccountManager apply(AccountManagerModel accountManagerModel) {
                Set<String> partners = accountManagerModel.getPartners();
                String partnersInJson = jsonConverter.toJson(partners);

                Set<String> endUsers = accountManagerModel.getEndUsers();
                String endUsersInJson = jsonConverter.toJson(endUsers);

                return new AccountManager(accountManagerModel.getId(), accountManagerModel.getName(), partnersInJson, endUsersInJson);
            }

        }));
        return accountManagers;
    }


}
