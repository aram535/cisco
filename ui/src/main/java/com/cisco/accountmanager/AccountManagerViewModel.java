package com.cisco.accountmanager;

import com.cisco.accountmanager.model.AccountManagerModel;
import com.cisco.accountmanager.service.AccountManagerService;
import com.cisco.exception.CiscoException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.zkoss.bind.BindUtils.postNotifyChange;

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
    private static final String DELETE_PARTNER = "deletePartner";
    private static final String ADD_PARTNER = "addPartner";
    private static final String DELETE_END_USER = "deleteEndUser";
    private static final String ADD_END_USER = "addEndUser";

    @WireVariable
    private AccountManagerService accountManagerService;

    private List<AccountManagerModel> allManagers = newArrayList();

    private AccountManagerModel selectedAccountManagerModel;
    private AccountManagerModel newAccountManagerModel = new AccountManagerModel();

    private String selectedPartner;
    private String newPartner;
    private String selectedEndUser;
    private String newEndUser;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<AccountManagerModel> getAllManagers() {
        try {
            allManagers = accountManagerService.getAccountManagers();
            return allManagers;
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
            notifyChange(this, "selectedAccountManagerModel");
        }
    }

    @Command(DELETE_PARTNER)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void deletePartner() {
        Set<String> partners = selectedAccountManagerModel.getPartners();
        if (!isEmpty(partners)) {
            boolean removed = partners.remove(selectedPartner);
            accountManagerService.saveOrUpdate(newArrayList(selectedAccountManagerModel));
            logger.debug("partner {} was deleted wit result {} from manager's {} list", selectedPartner, selectedAccountManagerModel, removed);
            notifyChange(this, "selectedAccountManagerModel");
        }
    }

    @Command(ADD_PARTNER)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void addPartner() {
        if (!isBlank(newPartner) && (selectedAccountManagerModel != null)) {
            Set<String> partners = selectedAccountManagerModel.getPartners();
            boolean added = partners.add(newPartner);
            accountManagerService.saveOrUpdate(newArrayList(selectedAccountManagerModel));
            logger.debug("partner {} was added wit result {} from manager's {} list", selectedPartner, selectedAccountManagerModel, added);
            notifyChange(this, "selectedAccountManagerModel");
        }
    }

    @Command(DELETE_END_USER)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void deleteEndUser() {
        Set<String> endUsers = selectedAccountManagerModel.getEndUsers();
        if (!isEmpty(endUsers)) {
            boolean removed = endUsers.remove(selectedEndUser);
            accountManagerService.saveOrUpdate(newArrayList(selectedAccountManagerModel));
            logger.debug("end user {} was deleted wit result {} from manager's {} list", selectedEndUser, selectedAccountManagerModel, removed);
            notifyChange(this, "selectedAccountManagerModel");
        }
    }

    @Command(ADD_END_USER)
    @NotifyChange(ALL_MANAGERS_NOTIFY)
    public void addEndUser() {
        if (!isBlank(newEndUser) && (selectedAccountManagerModel != null)) {
            Set<String> endUsers = selectedAccountManagerModel.getEndUsers();
            boolean added = endUsers.add(newEndUser);
            accountManagerService.saveOrUpdate(newArrayList(selectedAccountManagerModel));
            logger.debug("end user {} was added wit result {} from manager's {} list", selectedEndUser, selectedAccountManagerModel, added);
            notifyChange(this, "selectedAccountManagerModel");
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

    public String getNewPartner() {
        return newPartner;
    }

    public void setNewPartner(String newPartner) {
        this.newPartner = newPartner;
    }

    public String getNewEndUser() {
        return newEndUser;
    }

    public void setNewEndUser(String newEndUser) {
        this.newEndUser = newEndUser;
    }

    private void notifyChange(Object bean, String... properties) {

        for (String property : properties) {
            postNotifyChange(null, null, bean, property);
        }

    }
}
