package com.cisco.prepos.model;

import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Alf on 01.05.2014.
 */

@Component
public class PreposRestrictions {

    private String partnerName;
    private String shippedBillNumber;
    private Timestamp toDate;
    private Timestamp fromDate;
	private String partNumber;
	private String ok;
	private String accountManagerName;

	@Value("${years.interval}")
	private int yearsInterval;

	@Value("${months.interval}")
	private int monthsInterval;

	@Value("${days.interval}")
	private int daysInterval;

	public PreposRestrictions() {
	}

	@PostConstruct
	public void init() {
		Timestamp currentTime = new Timestamp(DateTimeUtils.currentTimeMillis());

		toDate = currentTime;

		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.YEAR, -yearsInterval);
		cal.add(Calendar.MONTH, -monthsInterval);
		cal.add(Calendar.DAY_OF_MONTH, -daysInterval);

		fromDate = new Timestamp(cal.getTime().getTime());
	}

	public PreposRestrictions(String partnerName, String shippedBillNumber, Timestamp toDate, Timestamp fromDate, String partNumber, String ok, String accountManagerName) {
		this.partnerName = partnerName;
		this.shippedBillNumber = shippedBillNumber;
		this.toDate = toDate;
		this.fromDate = fromDate;
		this.partNumber = partNumber;
		this.ok = ok;
		this.accountManagerName = accountManagerName;
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

	public String getPartNumber() {
		return partNumber;
	}

	public String getOk() {
		return ok;
	}

	public String getAccountManagerName() {
		return accountManagerName;
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

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}
}
