package com.cisco.prepos.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Alf on 05.04.14.
 */
public class Prepos {

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
    private long shippedDate;
    private String shippedBillNumber;
    private String sales;
    private String comment;
    private String serials;
    private int zip;

    Prepos(String type, String partnerName, String partNumber, double posSum, int quantity, boolean ok, int delta, int saleDiscount, int buyDiscount, double salePrice, double buyPrice, String firstPromo, String secondPromo, String endUser, String clientNumber, long shippedDate, String shippedBillNumber, String sales, String comment, String serials, int zip) {
        this.type = type;
        this.partnerName = partnerName;
        this.partNumber = partNumber;
        this.posSum = posSum;
        this.quantity = quantity;
        this.ok = ok;
        this.delta = delta;
        this.saleDiscount = saleDiscount;
        this.buyDiscount = buyDiscount;
        this.salePrice = salePrice;
        this.buyPrice = buyPrice;
        this.firstPromo = firstPromo;
        this.secondPromo = secondPromo;
        this.endUser = endUser;
        this.clientNumber = clientNumber;
        this.shippedDate = shippedDate;
        this.shippedBillNumber = shippedBillNumber;
        this.sales = sales;
        this.comment = comment;
        this.serials = serials;
        this.zip = zip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setPosSum(double posSum) {
        this.posSum = posSum;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public void setSaleDiscount(int saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public void setBuyDiscount(int buyDiscount) {
        this.buyDiscount = buyDiscount;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setFirstPromo(String firstPromo) {
        this.firstPromo = firstPromo;
    }

    public void setSecondPromo(String secondPromo) {
        this.secondPromo = secondPromo;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setShippedDate(long shippedDate) {
        this.shippedDate = shippedDate;
    }

    public void setShippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSerials(String serials) {
        this.serials = serials;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getType() {
        return type;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public double getPosSum() {
        return posSum;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isOk() {
        return ok;
    }

    public int getDelta() {
        return delta;
    }

    public int getSaleDiscount() {
        return saleDiscount;
    }

    public int getBuyDiscount() {
        return buyDiscount;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public String getFirstPromo() {
        return firstPromo;
    }

    public String getSecondPromo() {
        return secondPromo;
    }

    public String getEndUser() {
        return endUser;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public long getShippedDate() {
        return shippedDate;
    }

    public String getShippedBillNumber() {
        return shippedBillNumber;
    }

    public String getSales() {
        return sales;
    }

    public String getComment() {
        return comment;
    }

    public String getSerials() {
        return serials;
    }

    public int getZip() {
        return zip;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Prepos rhs = (Prepos) obj;
        return new EqualsBuilder()
                .append(this.type, rhs.type)
                .append(this.partnerName, rhs.partnerName)
                .append(this.partNumber, rhs.partNumber)
                .append(this.posSum, rhs.posSum)
                .append(this.quantity, rhs.quantity)
                .append(this.ok, rhs.ok)
                .append(this.delta, rhs.delta)
                .append(this.saleDiscount, rhs.saleDiscount)
                .append(this.buyDiscount, rhs.buyDiscount)
                .append(this.salePrice, rhs.salePrice)
                .append(this.buyPrice, rhs.buyPrice)
                .append(this.firstPromo, rhs.firstPromo)
                .append(this.secondPromo, rhs.secondPromo)
                .append(this.endUser, rhs.endUser)
                .append(this.clientNumber, rhs.clientNumber)
                .append(this.shippedDate, rhs.shippedDate)
                .append(this.shippedBillNumber, rhs.shippedBillNumber)
                .append(this.sales, rhs.sales)
                .append(this.comment, rhs.comment)
                .append(this.serials, rhs.serials)
                .append(this.zip, rhs.zip)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(partnerName)
                .append(partNumber)
                .append(posSum)
                .append(quantity)
                .append(ok)
                .append(delta)
                .append(saleDiscount)
                .append(buyDiscount)
                .append(salePrice)
                .append(buyPrice)
                .append(firstPromo)
                .append(secondPromo)
                .append(endUser)
                .append(clientNumber)
                .append(shippedDate)
                .append(shippedBillNumber)
                .append(sales)
                .append(comment)
                .append(serials)
                .append(zip)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
