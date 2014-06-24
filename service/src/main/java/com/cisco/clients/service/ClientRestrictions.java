package com.cisco.clients.service;

/**
 * Created by Alf on 24.06.2014.
 */
public class ClientRestrictions {

	private String clientNumber;
	private String partnerName;

	public ClientRestrictions() {
	}

	public ClientRestrictions(String clientNumber, String partnerName) {
		this.clientNumber = clientNumber;
		this.partnerName = partnerName;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
}
