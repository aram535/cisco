package com.cisco.darts.dto;

import java.sql.Timestamp;

public class DartBuilder {
	private long id;
	private String authorizationNumber;
	private int version;
	private String distributorInfo;
	private Timestamp startDate;
	private Timestamp endDate;
	private double distiDiscount;
	private String resellerName;
	private String resellerCountry;
	private int resellerAcct;
	private String endUserName;
	private String endUserCountry;
	private int quantity;
	private String ciscoSku;
	private String distiSku;
	private String listPrice;
	private String claimUnit;
	private String extCreditAmt;
	private String fastTrackPie;
	private String ipNgnPartnerPricingEm;
	private String mdmFulfillment;

	public static DartBuilder builder() {
		return new DartBuilder();
	}

	public DartBuilder setId(long id) {
		this.id = id;
		return this;
	}

	public DartBuilder setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
		return this;
	}

	public DartBuilder setVersion(int version) {
		this.version = version;
		return this;
	}

	public DartBuilder setDistributorInfo(String distributorInfo) {
		this.distributorInfo = distributorInfo;
		return this;
	}

	public DartBuilder setStartDate(Timestamp startDate) {
		this.startDate = startDate;
		return this;
	}

	public DartBuilder setEndDate(Timestamp endDate) {
		this.endDate = endDate;
		return this;
	}

	public DartBuilder setDistiDiscount(double distiDiscount) {
		this.distiDiscount = distiDiscount;
		return this;
	}

	public DartBuilder setResellerName(String resellerName) {
		this.resellerName = resellerName;
		return this;
	}

	public DartBuilder setResellerCountry(String resellerCountry) {
		this.resellerCountry = resellerCountry;
		return this;
	}

	public DartBuilder setResellerAcct(int resellerAcct) {
		this.resellerAcct = resellerAcct;
		return this;
	}

	public DartBuilder setEndUserName(String endUserName) {
		this.endUserName = endUserName;
		return this;
	}

	public DartBuilder setEndUserCountry(String endUserCountry) {
		this.endUserCountry = endUserCountry;
		return this;
	}

	public DartBuilder setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public DartBuilder setCiscoSku(String ciscoSku) {
		this.ciscoSku = ciscoSku;
		return this;
	}

	public DartBuilder setDistiSku(String distiSku) {
		this.distiSku = distiSku;
		return this;
	}

	public DartBuilder setListPrice(String listPrice) {
		this.listPrice = listPrice;
		return this;
	}

	public DartBuilder setClaimUnit(String claimUnit) {
		this.claimUnit = claimUnit;
		return this;
	}

	public DartBuilder setExtCreditAmt(String extCreditAmt) {
		this.extCreditAmt = extCreditAmt;
		return this;
	}

	public DartBuilder setFastTrackPie(String fastTrackPie) {
		this.fastTrackPie = fastTrackPie;
		return this;
	}

	public DartBuilder setIpNgnPartnerPricingEm(String ipNgnPartnerPricingEm) {
		this.ipNgnPartnerPricingEm = ipNgnPartnerPricingEm;
		return this;
	}

	public DartBuilder setMdmFulfillment(String mdmFulfillment) {
		this.mdmFulfillment = mdmFulfillment;
		return this;
	}

	public Dart build() {
		return new Dart(id, authorizationNumber, version, distributorInfo, startDate, endDate, distiDiscount, resellerName, resellerCountry, resellerAcct, endUserName, endUserCountry, quantity, ciscoSku, distiSku, listPrice, claimUnit, extCreditAmt, fastTrackPie, ipNgnPartnerPricingEm, mdmFulfillment);
	}
}