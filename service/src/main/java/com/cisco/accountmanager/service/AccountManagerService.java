package com.cisco.accountmanager.service;

import com.cisco.accountmanager.model.AccountManagerModel;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 22:38
 */
public interface AccountManagerService {

    List<AccountManagerModel> getAccountManagers();

    AccountManagerModel getAccountManagerByPartner(String partnerName);

    AccountManagerModel getAccountManagerByEndUser(String endUserName);
}
