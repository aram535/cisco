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
	private int quantityInitial;
    private String ciscoSku;
    private String distiSku;
    private double listPrice;
    private double claimUnit;
    private double extCreditAmt;
    private double fastTrackPie;
    private double ipNgnPartnerPricingEm;
    private double mdmFulfillment;

	public static DartBuilder builder() {
        return new DartBuilder();
    }

    public static DartBuilder builder(Dart dart) {
        DartBuilder builder = new DartBuilder();
        builder.id = dart.getId();
        builder.version = dart.getVersion();
        builder.authorizationNumber = dart.getAuthorizationNumber();
        builder.ciscoSku = dart.getCiscoSku();
        builder.claimUnit = dart.getClaimUnit();
        builder.distiDiscount = dart.getDistiDiscount();
        builder.distiSku = dart.getDistiSku();
        builder.distributorInfo = dart.getDistributorInfo();
        builder.endDate = dart.getEndDate();
        builder.startDate = dart.getStartDate();
        builder.endUserCountry = dart.getEndUserCountry();
        builder.endUserName = dart.getEndUserName();
        builder.extCreditAmt = dart.getExtCreditAmt();
        builder.fastTrackPie = dart.getFastTrackPie();
        builder.ipNgnPartnerPricingEm = dart.getIpNgnPartnerPricingEm();
        builder.mdmFulfillment = dart.getMdmFulfillment();
        builder.listPrice = dart.getListPrice();
        builder.quantity = dart.getQuantity();
	    builder.quantityInitial = dart.getQuantityInitial();
        builder.resellerAcct = dart.getResellerAcct();
        builder.resellerCountry = dart.getResellerCountry();
        builder.resellerName = dart.getResellerName();
        return builder;
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

    public DartBuilder setListPrice(double listPrice) {
        this.listPrice = listPrice;
        return this;
    }

    public DartBuilder setClaimUnit(double claimUnit) {
        this.claimUnit = claimUnit;
        return this;
    }

    public DartBuilder setExtCreditAmt(double extCreditAmt) {
        this.extCreditAmt = extCreditAmt;
        return this;
    }

    public DartBuilder setFastTrackPie(double fastTrackPie) {
        this.fastTrackPie = fastTrackPie;
        return this;
    }

    public DartBuilder setIpNgnPartnerPricingEm(double ipNgnPartnerPricingEm) {
        this.ipNgnPartnerPricingEm = ipNgnPartnerPricingEm;
        return this;
    }

    public DartBuilder setMdmFulfillment(double mdmFulfillment) {
        this.mdmFulfillment = mdmFulfillment;
        return this;
    }

	public DartBuilder setQuantityInitial(int quantityInitial) {
		this.quantityInitial = quantityInitial;
		return this;
	}

	public Dart build() {
        return new Dart(id, authorizationNumber, version, distributorInfo, startDate, endDate, distiDiscount, resellerName, resellerCountry, resellerAcct, endUserName, endUserCountry, quantity, quantityInitial, ciscoSku, distiSku, listPrice, claimUnit, extCreditAmt, fastTrackPie, ipNgnPartnerPricingEm, mdmFulfillment);
    }
}