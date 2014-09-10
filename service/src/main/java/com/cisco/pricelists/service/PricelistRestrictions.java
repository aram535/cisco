package com.cisco.pricelists.service;

/**
 * Created by Alf on 10.09.2014.
 */
public class PricelistRestrictions {

	private String partNumber;

	public PricelistRestrictions(String partNumber) {
		this.partNumber = partNumber;
	}

	public PricelistRestrictions() {
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
}
