package com.cisco.prepos.model;

import org.joda.time.DateTimeUtils;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Alf on 01.05.2014.
 */
public class PreposRestrictions {

    private String partnerName;
    private String shippedBillNumber;
    private Timestamp toDate;
    private Timestamp fromDate;

    public PreposRestrictions() {

	    toDate = new Timestamp(DateTimeUtils.currentTimeMillis());

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(toDate);
	    cal.add(Calendar.DAY_OF_WEEK, -7);

	    fromDate = new Timestamp(cal.getTime().getTime());
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
