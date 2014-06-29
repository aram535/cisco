package com.cisco.accountmanager;

import com.cisco.accountmanager.model.AccountManagerModel;
import com.cisco.accountmanager.service.AccountManagerService;
import com.google.common.collect.Lists;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.util.List;

/**
 * User: Rost
 * Date: 29.06.2014
 * Time: 16:35
 */
@VariableResolver(DelegatingVariableResolver.class)
public class AccountManagerViewModel {

    @WireVariable
    private AccountManagerService accountManagerService;

    private List<AccountManagerModel> accountManagers = Lists.newArrayList();

    private AccountManagerModel selectedAccountManagerModel;
    private AccountManagerModel newAccountManagerModel = new AccountManagerModel();

    public void setAccountManagerService(AccountManagerService accountManagerService) {
        this.accountManagerService = accountManagerService;
    }

    public List<AccountManagerModel> getAllManagers() {
        try {
            accountManagers = accountManagerService.getAccountManagers();
            return accountManagers;
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), null, 0, Messagebox.ERROR);
            return Lists.newArrayList();
        }
    }

    public AccountManagerModel getNewAccountManagerModel() {
        return newAccountManagerModel;
    }

    public void setNewAccountManagerModel(AccountManagerModel newAccountManagerModel) {
        this.newAccountManagerModel = newAccountManagerModel;
    }

    public AccountManagerModel getSelectedAccountManagerModel() {
        return selectedAccountManagerModel;
    }

    public void setSelectedAccountManagerModel(AccountManagerModel selectedAccountManagerModel) {
        this.selectedAccountManagerModel = selectedAccountManagerModel;
    }
}
