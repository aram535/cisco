package com.cisco.prepos.dto;

import java.sql.Timestamp;

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
    private int saleDiscount;
    private int buyDiscount;
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

    public PreposBuilder saleDiscount(int saleDiscount) {
        this.saleDiscount = saleDiscount;
        return this;
    }

    public PreposBuilder buyDiscount(int buyDiscount) {
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

    public Prepos build() {
        Prepos prepos = new Prepos(id, type, partnerName, partNumber, posSum, quantity, ok, delta, saleDiscount, buyDiscount, salePrice, buyPrice, firstPromo, secondPromo, endUser, clientNumber, shippedDate, shippedBillNumber, comment, serials, zip);
        return prepos;
    }
}
