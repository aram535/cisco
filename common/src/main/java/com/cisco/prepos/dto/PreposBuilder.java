package com.cisco.prepos.dto;

import java.sql.Timestamp;

import static com.cisco.prepos.dto.Prepos.Status;

/**
 * User: Rost
 * Date: 06.04.2014
 * Time: 23:07
 */
public class PreposBuilder {

    private long id;
    private String type;
    private String partnerName;
    private String partNumber;
    private double posSum;
    private int quantity;
    private boolean ok;
    private int delta;
    private double saleDiscount;
    private double buyDiscount;
    private double salePrice;
    private double buyPrice;
    private String firstPromo;
    private String secondPromo;
    private String endUser;
    private String clientNumber;
    private Timestamp shippedDate;
    private String shippedBillNumber;
    private String comment;
    private String serials;
    private int zip;
    private String accountManagerName;
    private Status status;
	private String posready_id;

    private PreposBuilder() {
    }

    public static PreposBuilder builder() {
        return new PreposBuilder();
    }


    public PreposBuilder id(long id) {
        this.id = id;
        return this;
    }

    public PreposBuilder type(String type) {
        this.type = type;
        return this;
    }

    public PreposBuilder partnerName(String partnerName) {
        this.partnerName = partnerName;
        return this;
    }

    public PreposBuilder partNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public PreposBuilder posSum(double posSum) {
        this.posSum = posSum;
        return this;
    }

    public PreposBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public PreposBuilder ok(boolean ok) {
        this.ok = ok;
        return this;
    }

    public PreposBuilder delta(int delta) {
        this.delta = delta;
        return this;
    }

    public PreposBuilder saleDiscount(double saleDiscount) {
        this.saleDiscount = saleDiscount;
        return this;
    }

    public PreposBuilder buyDiscount(double buyDiscount) {
        this.buyDiscount = buyDiscount;
        return this;
    }

    public PreposBuilder salePrice(double salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public PreposBuilder buyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public PreposBuilder firstPromo(String firstPromo) {
        this.firstPromo = firstPromo;
        return this;
    }

    public PreposBuilder secondPromo(String secondPromo) {
        this.secondPromo = secondPromo;
        return this;
    }

    public PreposBuilder endUser(String endUser) {
        this.endUser = endUser;
        return this;
    }

    public PreposBuilder clientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }

    public PreposBuilder shippedDate(Timestamp shippedDate) {
        this.shippedDate = shippedDate;
        return this;
    }

    public PreposBuilder shippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
        return this;
    }

    public PreposBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public PreposBuilder serials(String serials) {
        this.serials = serials;
        return this;
    }

    public PreposBuilder zip(int zip) {
        this.zip = zip;
        return this;
    }

    public PreposBuilder accountManagerName(String accountManagerName) {
        this.accountManagerName = accountManagerName;
        return this;
    }

    public PreposBuilder status(Status status) {
        this.status = status;
        return this;
    }
	public PreposBuilder posreadyId(String posready_id) {
		this.posready_id = posready_id;
		return this;
	}

    public Prepos build() {
        Prepos prepos = new Prepos(id, type, partnerName, partNumber, posSum, quantity, ok, delta, saleDiscount, buyDiscount, salePrice, buyPrice, firstPromo, secondPromo, endUser, clientNumber, shippedDate, shippedBillNumber, comment, serials, zip, accountManagerName, status, posready_id);
        return prepos;
    }

    public PreposBuilder prepos(Prepos prepos) {
        this.id = prepos.getId();
        this.type = prepos.getType();
        this.partnerName = prepos.getPartnerName();
        this.partNumber = prepos.getPartNumber();
        this.posSum = prepos.getPosSum();
        this.quantity = prepos.getQuantity();
        this.ok = prepos.getOk();
        this.delta = prepos.getDelta();
        this.saleDiscount = prepos.getSaleDiscount();
        this.buyDiscount = prepos.getBuyDiscount();
        this.salePrice = prepos.getSalePrice();
        this.buyPrice = prepos.getBuyPrice();
        this.firstPromo = prepos.getFirstPromo();
        this.secondPromo = prepos.getSecondPromo();
        this.endUser = prepos.getEndUser();
        this.clientNumber = prepos.getClientNumber();
        this.shippedDate = prepos.getShippedDate();
        this.shippedBillNumber = prepos.getShippedBillNumber();
        this.comment = prepos.getComment();
        this.serials = prepos.getSerials();
        this.zip = prepos.getZip();
        this.accountManagerName = prepos.getAccountManagerName();
        this.status = prepos.getStatus();
	    this.posready_id = prepos.getPosreadyId();
        return this;
    }
}
