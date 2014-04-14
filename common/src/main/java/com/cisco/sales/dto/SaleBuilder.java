package com.cisco.sales.dto;

import java.util.Date;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:48
 */
public class SaleBuilder {

    private long id;
    private Date shippedDate;
    private String shippedBillNumber;
    private String clientName;
    private String clientNumber;
    private String clientZip;
    private String partNumber;
    private int quantity;
    private String serials;
    private double price;
    private String ciscoType;
    private String comment;
    private Sale.Status status;

    private SaleBuilder() {
    }

    public static SaleBuilder builder() {
        return new SaleBuilder();
    }

    public SaleBuilder id(long id) {
        this.id = id;
        return this;
    }

    public SaleBuilder shippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
        return this;
    }

    public SaleBuilder shippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
        return this;
    }

    public SaleBuilder clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public SaleBuilder clientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }

    public SaleBuilder clientZip(String clientZip) {
        this.clientZip = clientZip;
        return this;
    }

    public SaleBuilder partNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public SaleBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public SaleBuilder serials(String serials) {
        this.serials = serials;
        return this;
    }

    public SaleBuilder price(double price) {
        this.price = price;
        return this;
    }

    public SaleBuilder ciscoType(String ciscoType) {
        this.ciscoType = ciscoType;
        return this;
    }

    public SaleBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public SaleBuilder status(Sale.Status status) {
        this.status = status;
        return this;
    }

    public Sale build() {
        Sale sale = new Sale(id, shippedDate, shippedBillNumber, clientName, clientNumber, clientZip, partNumber, quantity, serials, price, ciscoType, comment, status);
        return sale;
    }
}
