package com.cisco.clients;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientFilter;
import com.cisco.clients.service.ClientRestrictions;
import com.cisco.clients.service.ClientsService;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.util.List;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class ClientsViewModel {

    private static final String FILTER_CHANGED_COMMAND = "filterChanged";
    private static final String ALL_CLIENTS_NOTIFY = "allClients";
    private static final String SELECTED_EVENT = "selectedEvent";
    private static final String UPDATE_COMMAND = "update";
    private static final String DELETE_COMMAND = "delete";
    private static final String ADD_COMMAND = "add";

    private Client selectedClientModel;
    private Client newClientModel = new Client();

    private ClientRestrictions clientRestrictions = new ClientRestrictions();

    @WireVariable
    private ClientFilter clientFilter;

    @WireVariable
    private ClientsService clientsService;

    private List<Client> allClients;
    private List<Client> filteredClients;

    public ClientRestrictions getClientRestrictions() {
        return clientRestrictions;
    }

    public Client getSelectedClientModel() {
        return selectedClientModel;
    }

    public Client getNewClientModel() {
        return newClientModel;
    }

    public void setSelectedClientModel(Client selectedClientModel) {
        this.selectedClientModel = selectedClientModel;
    }

    public void setNewClientModel(Client newClientModel) {
        this.newClientModel = newClientModel;
    }

    public void setClientsService(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    public List<Client> getAllClients() {
        try {
            allClients = clientsService.getClients();
            filteredClients = clientFilter.filter(allClients, clientRestrictions);
            return filteredClients;
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), null, 0, Messagebox.ERROR);
            return Lists.newArrayList();
        }
    }

    @Command(ADD_COMMAND)
    @NotifyChange(ALL_CLIENTS_NOTIFY)
    public void add() {

        try {
            clientsService.save(newClientModel);
            this.newClientModel = new Client();
        } catch (Exception e) {
            throw new CiscoException(ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    @Command(UPDATE_COMMAND)
    @NotifyChange(ALL_CLIENTS_NOTIFY)
    public void update() {
        clientsService.update(selectedClientModel);
    }

    @Command(DELETE_COMMAND)
    @NotifyChange({ALL_CLIENTS_NOTIFY, SELECTED_EVENT})
    public void delete() {
        //shouldn't be able to delete with selectedEvent being null anyway
        //unless trying to hack the system, so just ignore the request
        if (this.selectedClientModel != null) {
            clientsService.delete(this.selectedClientModel);
            this.selectedClientModel = null;
        }
    }

    @Command(FILTER_CHANGED_COMMAND)
    @NotifyChange(ALL_CLIENTS_NOTIFY)
    public void filterChanged() {
        filteredClients = clientFilter.filter(allClients, clientRestrictions);
    }
}
