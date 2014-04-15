package com.cisco.clients;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

/**
 * Created by Alf on 07.04.14.
 */

@VariableResolver(DelegatingVariableResolver.class)
public class ClientsViewModel {

	private Client selectedClientModel;
	private Client newClientModel = new Client();

	@WireVariable
	private ClientsService clientsService;

	private List<Client> allClients;

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
		allClients = clientsService.getAllData();
		return allClients;
	}

	@Command("add")
	@NotifyChange("allClients")
	public void add() {

		clientsService.save(newClientModel);
		this.newClientModel = new Client();
	}

	@Command("update")
	@NotifyChange("allClients")
	public void update() {
		clientsService.update(selectedClientModel);
	}

	@Command("delete")
	@NotifyChange({"allClients", "selectedEvent"})
	public void delete() {
		//shouldn't be able to delete with selectedEvent being null anyway
		//unless trying to hack the system, so just ignore the request
		if(this.selectedClientModel != null) {
			clientsService.delete(this.selectedClientModel);
			this.selectedClientModel = null;
		}
	}
}
