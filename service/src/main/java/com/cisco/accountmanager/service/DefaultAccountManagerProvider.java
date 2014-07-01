package com.cisco.accountmanager.service;

import com.cisco.accountmanager.model.AccountManagerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cisco.accountmanager.service.DefaultAccountManagerService.DEFAULT_MANAGER;

/**
 * User: Rost
 * Date: 28.06.2014
 * Time: 15:46
 */
@Component
public class DefaultAccountManagerProvider implements AccountManagerProvider {

    @Autowired
    private AccountManagerService accountManagerService;

    @Override
    public AccountManagerModel getAccountManager(String partnerName, String endUserName) {

        AccountManagerModel accountManagerByPartner = accountManagerService.getAccountManagerByPartner(partnerName);

        if (accountManagerByPartner.equals(DEFAULT_MANAGER)) {
            return accountManagerService.getAccountManagerByEndUser(endUserName);
        }

        return accountManagerByPartner;
    }

    void setAccountManagerService(AccountManagerService accountManagerService) {
        this.accountManagerService = accountManagerService;
    }
}
