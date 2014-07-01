package com.cisco.accountmanager.service;

import com.cisco.accountmanager.model.AccountManagerModel;

/**
 * User: Rost
 * Date: 28.06.2014
 * Time: 15:43
 */
public interface AccountManagerProvider {
    AccountManagerModel getAccountManager(String partnerName, String endUserName);
}
