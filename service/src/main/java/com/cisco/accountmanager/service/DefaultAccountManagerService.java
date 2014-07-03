package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dao.AccountManagerDao;
import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 22:44
 */
@Service("accountManagerService")
public class DefaultAccountManagerService implements AccountManagerService {

    final static AccountManagerModel DEFAULT_MANAGER = new AccountManagerModel(-1L, "Default manager", Sets.<String>newHashSet(), Sets.<String>newHashSet());

    @Autowired
    private AccountManagerDao accountManagerDao;

    @Autowired
    private AccountManagerModelFactory accountManagerModelFactory;

    private List<AccountManagerModel> accountManagerModels = newArrayList();
    private Map<String, AccountManagerModel> partnerNameToManagersMap = newHashMap();
    private Map<String, AccountManagerModel> endUserNameToManagersMap = newHashMap();

    @Override
    public List<AccountManagerModel> getAccountManagers() {
        return accountManagerModels;
    }

    @Override
    public AccountManagerModel getAccountManagerByPartner(String partnerName) {
        return getAccountManagerModelFromMap(partnerNameToManagersMap, partnerName);
    }

    @Override
    public AccountManagerModel getAccountManagerByEndUser(String endUserName) {
        return getAccountManagerModelFromMap(endUserNameToManagersMap, endUserName);
    }

    @Override
    public void saveOrUpdate(List<AccountManagerModel> accountManagerModels) {
        List<AccountManager> managers = accountManagerModelFactory.createManagers(accountManagerModels);
        accountManagerDao.saveOrUpdate(managers);
        fetchModels();
    }

    @Override
    public void delete(AccountManagerModel accountManagerModel) {
        List<AccountManager> managers = accountManagerModelFactory.createManagers(newArrayList(accountManagerModel));
        boolean managersIsNotEmpty = !CollectionUtils.isEmpty(managers);
        if (managersIsNotEmpty) {
            AccountManager accountManager = managers.get(0);
            accountManagerDao.delete(accountManager);
            fetchModels();
        }
    }

    @PostConstruct
    public void init() {
        fetchModels();
    }

    private AccountManagerModel getAccountManagerModelFromMap(Map<String, AccountManagerModel> map, String key) {
        AccountManagerModel accountManagerModel = map.get(key);

        if (accountManagerModel != null) {
            return accountManagerModel;
        }

        return DEFAULT_MANAGER;
    }

    private void fetchModels() {
        List<AccountManager> accountManagers = accountManagerDao.getAccountManagers();
        boolean accountManagersListIsNotEmpty = !CollectionUtils.isEmpty(accountManagers);
        if (accountManagersListIsNotEmpty) {
            accountManagerModels = accountManagerModelFactory.createModels(accountManagers);
            initMaps();
        }
    }

    private void initMaps() {
        Map<String, AccountManagerModel> partnerNameToManagersMap = newHashMap();
        Map<String, AccountManagerModel> endUserNameToManagersMap = newHashMap();
        for (AccountManagerModel accountManagerModel : accountManagerModels) {
            for (String partnerName : accountManagerModel.getPartners()) {
                partnerNameToManagersMap.put(partnerName, accountManagerModel);
            }
            for (String endUserName : accountManagerModel.getEndUsers()) {
                endUserNameToManagersMap.put(endUserName, accountManagerModel);
            }
        }
        this.partnerNameToManagersMap = partnerNameToManagersMap;
        this.endUserNameToManagersMap = endUserNameToManagersMap;
    }
}
