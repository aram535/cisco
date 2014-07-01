package com.cisco.accountmanager.service;

import com.cisco.accountmanager.dto.AccountManager;
import com.cisco.accountmanager.model.AccountManagerModel;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 23:29
 */
public interface AccountManagerModelFactory {

    List<AccountManagerModel> createModels(List<AccountManager> accountManagers);

    List<AccountManager> createManagers(List<AccountManagerModel> accountManagerModels);
}
