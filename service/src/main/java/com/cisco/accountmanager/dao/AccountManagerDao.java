package com.cisco.accountmanager.dao;

import com.cisco.accountmanager.dto.AccountManager;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 21:31
 */
public interface AccountManagerDao {
    List<AccountManager> getAccountManagers();
}
