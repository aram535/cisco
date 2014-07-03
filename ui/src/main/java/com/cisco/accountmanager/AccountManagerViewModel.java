package com.cisco.accountmanager;

import com.cisco.accountmanager.model.AccountManagerModel;
import com.cisco.accountmanager.service.AccountManagerService;
import com.cisco.exception.CiscoException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Rost
 * Date: 29.06.2014
 * Time: 16:35
 */
@VariableResolver(DelegatingVariableResolver.class)
public class AccountManagerViewModel {

    private static final String ALL_MANAGERS_NOTIFY = "allManagers";
    private static final String UPDATE_COMMAND = "update";
    private static final String DELETE_COMMAND = "delete";
    private static final String ADD_COMMAND = "add";

    @WireVariable
    private AccountManagerService accountManagerService;

    private List<AccountManagerModel> accountManagers = newArrayList();

    private AccountManagerModel selectedAccountManagerModel;
    private AccountManagerModel newAccountManagerModel = new AccountManagerModel();

    private String selectedPartner;
    private String selectedEndUser;

    public List<AccountManagerModel> getAllManagers() {
        try {
            accountManagers = accountManagerService.getAccountManagers();
            return accountManagers;
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), null, 0, Messagebox.ERROR);
            return newArrayList();
        }
    }

    @Command(ADD_COMMAND)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void add() {
        try {
            accountManagerService.saveOrUpdate(newArrayList(newAccountManagerModel));
            this.newAccountManagerModel = new AccountManagerModel();
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    @Command(UPDATE_COMMAND)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void update() {
        accountManagerService.saveOrUpdate(newArrayList(selectedAccountManagerModel));
    }

    @Command(DELETE_COMMAND)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void delete() {
        if (this.selectedAccountManagerModel != null) {
            accountManagerService.delete(this.selectedAccountManagerModel);
            this.selectedAccountManagerModel = null;
        }
    }

    public void setAccountManagerService(AccountManagerService accountManagerService) {
        this.accountManagerService = accountManagerService;
    }

    public void setNewAccountManagerModel(AccountManagerModel newAccountManagerModel) {
        this.newAccountManagerModel = newAccountManagerModel;
    }

    public void setSelectedAccountManagerModel(AccountManagerModel selectedAccountManagerModel) {
        this.selectedAccountManagerModel = selectedAccountManagerModel;
    }

    public AccountManagerModel getNewAccountManagerModel() {
        return newAccountManagerModel;
    }

    public AccountManagerModel getSelectedAccountManagerModel() {
        return selectedAccountManagerModel;
    }

    public String getSelectedPartner() {
        return selectedPartner;
    }

    public void setSelectedPartner(String selectedPartner) {
        this.selectedPartner = selectedPartner;
    }

    public String getSelectedEndUser() {
        return selectedEndUser;
    }

    public void setSelectedEndUser(String selectedEndUser) {
        this.selectedEndUser = selectedEndUser;
    }
}
