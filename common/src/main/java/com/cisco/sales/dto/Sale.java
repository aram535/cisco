package com.cisco.sales.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:28
 */
@Entity(name = "sale")
public class Sale {

    private long id;
    private Timestamp shippedDate;
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
    private Status status;

    public Sale() {

    }

    public Sale(long id, Timestamp shippedDate, String shippedBillNumber, String clientName, String clientNumber, String clientZip, String partNumber, int quantity, String serials, double price, String ciscoType, String comment, Status status) {
        this.id = id;
        this.shippedDate = shippedDate;
        this.shippedBillNumber = shippedBillNumber;
        this.clientName = clientName;
        this.clientNumber = clientNumber;
        this.clientZip = clientZip;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.serials = serials;
        this.price = price;
        this.ciscoType = ciscoType;
        this.comment = comment;
        this.status = status;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    @Column(name = "shipped_date")
    public Date getShippedDate() {
        return shippedDate;
    }

    @Column(name = "shipped_bill_number")
    public String getShippedBillNumber() {
        return shippedBillNumber;
    }

    @Column(name = "client_name")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "client_number")
    public String getClientNumber() {
        return clientNumber;
    }

    @Column(name = "client_zip")
    public String getClientZip() {
        return clientZip;
    }

    @Column(name = "part_number")
    public String getPartNumber() {
        return partNumber;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    @Column(name = "serials")
    public String getSerials() {
        return serials;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    @Column(name = "cisco_type")
    public String getCiscoType() {
        return ciscoType;
    }

    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setShippedDate(Timestamp shippedDate) {
        this.shippedDate = shippedDate;
    }

    public void setShippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setClientZip(String clientZip) {
        this.clientZip = clientZip;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSerials(String serials) {
        this.serials = serials;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCiscoType(String ciscoType) {
        this.ciscoType = ciscoType;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        Sale rhs = (Sale) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.shippedDate, rhs.shippedDate)
                .append(this.shippedBillNumber, rhs.shippedBillNumber)
                .append(this.clientName, rhs.clientName)
                .append(this.clientNumber, rhs.clientNumber)
                .append(this.clientZip, rhs.clientZip)
                .append(this.partNumber, rhs.partNumber)
                .append(this.quantity, rhs.quantity)
                .append(this.serials, rhs.serials)
                .append(this.price, rhs.price)
                .append(this.ciscoType, rhs.ciscoType)
                .append(this.comment, rhs.comment)
                .append(this.status, rhs.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(shippedDate)
                .append(shippedBillNumber)
                .append(clientName)
                .append(clientNumber)
                .append(clientZip)
                .append(partNumber)
                .append(quantity)
                .append(serials)
                .append(price)
                .append(ciscoType)
                .append(comment)
                .append(status)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public enum Status {
        NOT_PROCESSED, PROCESSED, WAITING;
    }
}
