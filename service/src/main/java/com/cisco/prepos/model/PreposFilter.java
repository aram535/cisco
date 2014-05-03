package com.cisco.prepos.model;

/**
 * Created by Alf on 01.05.2014.
 */
public class PreposFilter {

	private String partnerName="";
	private String shippedBillNumber="";

	public String getPartnerName() {
		return partnerName;
	}

	public String getShippedBillNumber() {
		return shippedBillNumber;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName == null ? "" : partnerName.trim();
	}

	public void setShippedBillNumber(String shippedBillNumber) {
		this.shippedBillNumber = shippedBillNumber == null ? "" : shippedBillNumber.trim();
	}
}
