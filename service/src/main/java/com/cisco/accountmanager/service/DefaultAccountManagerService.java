package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dao.AccountManagerDao;
import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 22:44
 */
@Service("accountManagerService")
public class DefaultAccountManagerService implements AccountManagerService {

    @Autowired
    private AccountManagerDao accountManagerDao;

    @Autowired
    private AccountManagerModelFactory accountManagerModelFactory;

    private List<AccountManagerModel> accountManagerModels = newArrayList();

    @Override
    public List<AccountManagerModel> getAccountManagers() {
        return accountManagerModels;
    }

    @PostConstruct
    public void init() {
        fetchModels();
    }

    private void fetchModels() {
        List<AccountManager> accountManagers = accountManagerDao.getAccountManagers();
        boolean accountManagersListIsNotEmpty = !CollectionUtils.isEmpty(accountManagers);
        if (accountManagersListIsNotEmpty) {
            accountManagerModels = accountManagerModelFactory.createModels(accountManagers);
        }
    }
}
