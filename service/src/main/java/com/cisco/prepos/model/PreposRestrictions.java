package com.cisco.prepos.model;

import java.sql.Timestamp;

/**
 * Created by Alf on 01.05.2014.
 */
public class PreposRestrictions {

    private String partnerName;
    private String shippedBillNumber;
    private Timestamp toDate;
    private Timestamp fromDate;

    public PreposRestrictions() {

    }

    public PreposRestrictions(String partnerName, String shippedBillNumber, Timestamp toDate, Timestamp fromDate) {
        this.partnerName = partnerName;
        this.shippedBillNumber = shippedBillNumber;
        this.toDate = toDate;
        this.fromDate = fromDate;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getShippedBillNumber() {
        return shippedBillNumber;
    }

    public Timestamp getToDate() {
        return toDate;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setShippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
    }

    public void setToDate(Timestamp toDate) {
        this.toDate = toDate;
    }

    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }
}
