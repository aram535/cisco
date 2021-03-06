package com.cisco.darts.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Alf on 15.04.14.
 */
@Entity(name = "dart")
@IdClass(Dart.DartId.class)
public class Dart {

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

    public Dart(long id, String authorizationNumber, int version, String distributorInfo, Timestamp startDate, Timestamp endDate,
                double distiDiscount, String resellerName, String resellerCountry, int resellerAcct, String endUserName,
                String endUserCountry, int quantity, int quantityInitial, String ciscoSku, String distiSku, double listPrice,
                double claimUnit, double extCreditAmt, double fastTrackPie, double ipNgnPartnerPricingEm,
                double mdmFulfillment) {
        this.id = id;
        this.authorizationNumber = authorizationNumber;
        this.version = version;
        this.distributorInfo = distributorInfo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.distiDiscount = distiDiscount;
        this.resellerName = resellerName;
        this.resellerCountry = resellerCountry;
        this.resellerAcct = resellerAcct;
        this.endUserName = endUserName;
        this.endUserCountry = endUserCountry;
        this.quantity = quantity;
        this.quantityInitial = quantityInitial;
        this.ciscoSku = ciscoSku;
        this.distiSku = distiSku;
        this.listPrice = listPrice;
        this.claimUnit = claimUnit;
        this.extCreditAmt = extCreditAmt;
        this.fastTrackPie = fastTrackPie;
        this.ipNgnPartnerPricingEm = ipNgnPartnerPricingEm;
        this.mdmFulfillment = mdmFulfillment;
    }

    public Dart(String authorizationNumber, String endUserName) {
        this.authorizationNumber = authorizationNumber;
        this.endUserName = endUserName;
    }

    public Dart() {

    }

    @Id
    @AttributeOverrides({
            @AttributeOverride(name = "ciscoSku",
                    column = @Column(name = "cisko_sku")),
            @AttributeOverride(name = "authorizationNumber",
                    column = @Column(name = "authorization_number"))
    })

    //@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

	@NaturalId
    @Column(name = "authorization_number")
    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    @Column(name = "distributor_info")
    public String getDistributorInfo() {
        return distributorInfo;
    }

    @Column(name = "start_date")
    public Timestamp getStartDate() {
        return startDate;
    }

    @Column(name = "end_date")
    public Timestamp getEndDate() {
        return endDate;
    }

    @Column(name = "disti_discount")
    public double getDistiDiscount() {
        return distiDiscount;
    }

    @Column(name = "reseller_name")
    public String getResellerName() {
        return resellerName;
    }

    @Column(name = "reseller_country")
    public String getResellerCountry() {
        return resellerCountry;
    }

    @Column(name = "reseller_acct")
    public int getResellerAcct() {
        return resellerAcct;
    }

    @Column(name = "end_user_name")
    public String getEndUserName() {
        return endUserName;
    }

    @Column(name = "end_user_country")
    public String getEndUserCountry() {
        return endUserCountry;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    @Column(name = "quantity_initial")
    public int getQuantityInitial() {
        return quantityInitial;
    }

    @NaturalId
    @Column(name = "cisco_sku")
    public String getCiscoSku() {
        return ciscoSku;
    }

    @Column(name = "disti_sku")
    public String getDistiSku() {
        return distiSku;
    }

    @Column(name = "list_price")
    public double getListPrice() {
        return listPrice;
    }

    @Column(name = "claim_unit")
    public double getClaimUnit() {
        return claimUnit;
    }

    @Column(name = "ext_credit_amt")
    public double getExtCreditAmt() {
        return extCreditAmt;
    }

    @Column(name = "fast_track_pie")
    public double getFastTrackPie() {
        return fastTrackPie;
    }

    @Column(name = "ip_ngn_partner_pricing_em")
    public double getIpNgnPartnerPricingEm() {
        return ipNgnPartnerPricingEm;
    }

    @Column(name = "mdm_fulfillment")
    public double getMdmFulfillment() {
        return mdmFulfillment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setDistributorInfo(String distributorInfo) {
        this.distributorInfo = distributorInfo;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setDistiDiscount(double distiDiscount) {
        this.distiDiscount = distiDiscount;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public void setResellerCountry(String resellerCountry) {
        this.resellerCountry = resellerCountry;
    }

    public void setResellerAcct(int resellerAcct) {
        this.resellerAcct = resellerAcct;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    public void setEndUserCountry(String endUserCountry) {
        this.endUserCountry = endUserCountry;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setQuantityInitial(int quantityInitial) {
        this.quantityInitial = quantityInitial;
    }

    public void setCiscoSku(String ciscoSku) {
        this.ciscoSku = ciscoSku;
    }

    public void setDistiSku(String distiSku) {
        this.distiSku = distiSku;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public void setClaimUnit(double claimUnit) {
        this.claimUnit = claimUnit;
    }

    public void setExtCreditAmt(double extCreditAmt) {
        this.extCreditAmt = extCreditAmt;
    }

    public void setFastTrackPie(double fastTrackPie) {
        this.fastTrackPie = fastTrackPie;
    }

    public void setIpNgnPartnerPricingEm(double ipNgnPartnerPricingEm) {
        this.ipNgnPartnerPricingEm = ipNgnPartnerPricingEm;
    }

    public void setMdmFulfillment(double mdmFulfillment) {
        this.mdmFulfillment = mdmFulfillment;
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
        Dart rhs = (Dart) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.authorizationNumber, rhs.authorizationNumber)
                .append(this.version, rhs.version)
                .append(this.distributorInfo, rhs.distributorInfo)
                .append(this.startDate, rhs.startDate)
                .append(this.endDate, rhs.endDate)
                .append(this.distiDiscount, rhs.distiDiscount)
                .append(this.resellerName, rhs.resellerName)
                .append(this.resellerCountry, rhs.resellerCountry)
                .append(this.resellerAcct, rhs.resellerAcct)
                .append(this.endUserName, rhs.endUserName)
                .append(this.endUserCountry, rhs.endUserCountry)
                .append(this.quantity, rhs.quantity)
                .append(this.quantityInitial, rhs.quantityInitial)
                .append(this.ciscoSku, rhs.ciscoSku)
                .append(this.distiSku, rhs.distiSku)
                .append(this.listPrice, rhs.listPrice)
                .append(this.claimUnit, rhs.claimUnit)
                .append(this.extCreditAmt, rhs.extCreditAmt)
                .append(this.fastTrackPie, rhs.fastTrackPie)
                .append(this.ipNgnPartnerPricingEm, rhs.ipNgnPartnerPricingEm)
                .append(this.mdmFulfillment, rhs.mdmFulfillment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(authorizationNumber)
                .append(version)
                .append(distributorInfo)
                .append(startDate)
                .append(endDate)
                .append(distiDiscount)
                .append(resellerName)
                .append(resellerCountry)
                .append(resellerAcct)
                .append(endUserName)
                .append(endUserCountry)
                .append(quantity)
                .append(quantityInitial)
                .append(ciscoSku)
                .append(distiSku)
                .append(listPrice)
                .append(claimUnit)
                .append(extCreditAmt)
                .append(fastTrackPie)
                .append(ipNgnPartnerPricingEm)
                .append(mdmFulfillment)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("authorizationNumber", authorizationNumber)
                .append("version", version)
                .append("distributorInfo", distributorInfo)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("distiDiscount", distiDiscount)
                .append("resellerName", resellerName)
                .append("resellerCountry", resellerCountry)
                .append("resellerAcct", resellerAcct)
                .append("endUserName", endUserName)
                .append("endUserCountry", endUserCountry)
                .append("quantity", quantity)
                .append("quantityInitial", quantityInitial)
                .append("ciscoSku", ciscoSku)
                .append("distiSku", distiSku)
                .append("listPrice", listPrice)
                .append("claimUnit", claimUnit)
                .append("extCreditAmt", extCreditAmt)
                .append("fastTrackPie", fastTrackPie)
                .append("ipNgnPartnerPricingEm", ipNgnPartnerPricingEm)
                .append("mdmFulfillment", mdmFulfillment)
                .toString();
    }

	public void copyFrom(Dart dart) {

		this.version = dart.getVersion();
		this.authorizationNumber = dart.getAuthorizationNumber();
		this.ciscoSku = dart.getCiscoSku();
		this.claimUnit = dart.getClaimUnit();
		this.distiDiscount = dart.getDistiDiscount();
		this.distiSku = dart.getDistiSku();
		this.distributorInfo = dart.getDistributorInfo();
		this.endDate = dart.getEndDate();
		this.startDate = dart.getStartDate();
		this.endUserCountry = dart.getEndUserCountry();
		this.endUserName = dart.getEndUserName();
		this.extCreditAmt = dart.getExtCreditAmt();
		this.fastTrackPie = dart.getFastTrackPie();
		this.ipNgnPartnerPricingEm = dart.getIpNgnPartnerPricingEm();
		this.mdmFulfillment = dart.getMdmFulfillment();
		this.listPrice = dart.getListPrice();
		this.quantity = dart.getQuantity();
		this.quantityInitial = dart.getQuantityInitial();
		this.resellerAcct = dart.getResellerAcct();
		this.resellerCountry = dart.getResellerCountry();
		this.resellerName = dart.getResellerName();
	}

	public static class DartId implements Serializable {

        private String ciscoSku;

        private String authorizationNumber;

        public DartId() {

        }

        public String getCiscoSku() {
            return ciscoSku;
        }

        public String getAuthorizationNumber() {
            return authorizationNumber;
        }

        public void setCiscoSku(String ciscoSku) {
            this.ciscoSku = ciscoSku;
        }

        public void setAuthorizationNumber(String authorizationNumber) {
            this.authorizationNumber = authorizationNumber;
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
            DartId rhs = (DartId) obj;
            return new EqualsBuilder()
                    .append(this.ciscoSku, rhs.ciscoSku)
                    .append(this.authorizationNumber, rhs.authorizationNumber)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(ciscoSku)
                    .append(authorizationNumber)
                    .toHashCode();
        }
    }
}
