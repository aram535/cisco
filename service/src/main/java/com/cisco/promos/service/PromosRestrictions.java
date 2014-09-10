package com.cisco.promos.service;

/**
 * Created by Alf on 10.09.2014.
 */
public class PromosRestrictions {

	private String partNumber;

	public PromosRestrictions(String partNumber) {
		this.partNumber = partNumber;
	}

	public PromosRestrictions() {
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
}
