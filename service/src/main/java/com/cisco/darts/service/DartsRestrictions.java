package com.cisco.darts.service;

import org.springframework.stereotype.Component;

/**
 * Created by Alf on 03.08.2014.
 */
@Component
public class DartsRestrictions {

	private String resellerName;
	private String endUserName;
	private String ciscoSku;
	private String authNumber;

	public DartsRestrictions() {
	}

	public DartsRestrictions(String resellerName, String endUserName, String ciscoSku, String authNumber) {
		this.resellerName = resellerName;
		this.endUserName = endUserName;
		this.ciscoSku = ciscoSku;
		this.authNumber = authNumber;
	}

	public String getResellerName() {
		return resellerName;
	}

	public String getEndUserName() {
		return endUserName;
	}

	public String getCiscoSku() {
		return ciscoSku;
	}

	public String getAuthNumber() {
		return authNumber;
	}

	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}

	public void setEndUserName(String endUserName) {
		this.endUserName = endUserName;
	}

	public void setCiscoSku(String ciscoSku) {
		this.ciscoSku = ciscoSku;
	}

	public void setAuthNumber(String authNumber) {
		this.authNumber = authNumber;
	}

}
