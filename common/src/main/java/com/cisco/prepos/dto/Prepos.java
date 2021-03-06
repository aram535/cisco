package com.cisco.prepos.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Alf on 05.04.14.
 */

@Entity(name = "pre_pos")
public class Prepos {

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
    private String posreadyId;
    private long claimId;
    private long batchId;

    public Prepos() {
    }

	public Prepos(long id, String type, String partnerName, String partNumber, double posSum, int quantity, boolean ok, int delta, double saleDiscount, double buyDiscount, double salePrice, double buyPrice, String firstPromo, String secondPromo, String endUser, String clientNumber, Timestamp shippedDate, String shippedBillNumber, String comment, String serials, int zip, String accountManagerName, Status status, String posreadyId, long claimId, long batchId) {
		this.id = id;
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
		this.comment = comment;
		this.serials = serials;
		this.zip = zip;
		this.accountManagerName = accountManagerName;
		this.status = status;
		this.posreadyId = posreadyId;
		this.claimId = claimId;
		this.batchId = batchId;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    @Column(name = "partner_name")
    public String getPartnerName() {
        return partnerName;
    }

    @Column(name = "part_number")
    public String getPartNumber() {
        return partNumber;
    }

    @Column(name = "pos_sum")
    public double getPosSum() {
        return posSum;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    @Column(name = "ok")
    public boolean getOk() {
        return ok;
    }

    @Column(name = "delta")
    public int getDelta() {
        return delta;
    }

    @Column(name = "sale_discount")
    public double getSaleDiscount() {
        return saleDiscount;
    }

    @Column(name = "buy_discount")
    public double getBuyDiscount() {
        return buyDiscount;
    }

    @Column(name = "sale_price")
    public double getSalePrice() {
        return salePrice;
    }

    @Column(name = "buy_price")
    public double getBuyPrice() {
        return buyPrice;
    }

    @Column(name = "promo1")
    public String getFirstPromo() {
        return firstPromo;
    }

    @Column(name = "promo2")
    public String getSecondPromo() {
        return secondPromo;
    }

    @Column(name = "end_user")
    public String getEndUser() {
        return endUser;
    }

    @Column(name = "client_number")
    public String getClientNumber() {
        return clientNumber;
    }

    @Column(name = "shipped_Date")
    public Timestamp getShippedDate() {
        return shippedDate;
    }

    @Column(name = "shipped_bill_number")
    public String getShippedBillNumber() {
        return shippedBillNumber;
    }

    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    @Column(name = "serials")
    public String getSerials() {
        return serials;
    }

    @Column(name = "zip")
    public int getZip() {
        return zip;
    }

    @Column(name = "account_manager_name")
    public String getAccountManagerName() {
        return accountManagerName;
    }

    @Column(name = "status")
    public Status getStatus() {
        return status;
    }

	@Column(name = "posready_id")
	public String getPosreadyId() {
		return posreadyId;
	}

	@Column(name = "claim_id")
	public long getClaimId() {
		return claimId;
	}
	@Column(name = "batch_id")
	public long getBatchId() {
		return batchId;
	}

	public void setId(long id) {
        this.id = id;
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

    public void setSaleDiscount(double saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public void setBuyDiscount(double buyDiscount) {
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

    public void setShippedDate(Timestamp shippedDate) {
        this.shippedDate = shippedDate;
    }

    public void setShippedBillNumber(String shippedBillNumber) {
        this.shippedBillNumber = shippedBillNumber;
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

    public void setAccountManagerName(String accountManagerName) {
        this.accountManagerName = accountManagerName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	public void setPosreadyId(String posreadyId) {
		this.posreadyId = posreadyId;
	}

	public void setClaimId(long claimId) {
		this.claimId = claimId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
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
				.append(this.id, rhs.id)
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
				.append(this.comment, rhs.comment)
				.append(this.serials, rhs.serials)
				.append(this.zip, rhs.zip)
				.append(this.accountManagerName, rhs.accountManagerName)
				.append(this.status, rhs.status)
				.append(this.posreadyId, rhs.posreadyId)
				.append(this.claimId, rhs.claimId)
				.append(this.batchId, rhs.batchId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
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
				.append(comment)
				.append(serials)
				.append(zip)
				.append(accountManagerName)
				.append(status)
				.append(posreadyId)
				.append(claimId)
				.append(batchId)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("type", type)
				.append("partnerName", partnerName)
				.append("partNumber", partNumber)
				.append("posSum", posSum)
				.append("quantity", quantity)
				.append("ok", ok)
				.append("delta", delta)
				.append("saleDiscount", saleDiscount)
				.append("buyDiscount", buyDiscount)
				.append("salePrice", salePrice)
				.append("buyPrice", buyPrice)
				.append("firstPromo", firstPromo)
				.append("secondPromo", secondPromo)
				.append("endUser", endUser)
				.append("clientNumber", clientNumber)
				.append("shippedDate", shippedDate)
				.append("shippedBillNumber", shippedBillNumber)
				.append("comment", comment)
				.append("serials", serials)
				.append("zip", zip)
				.append("accountManagerName", accountManagerName)
				.append("status", status)
				.append("posreadyId", posreadyId)
				.append("claimId", claimId)
				.append("batchId", batchId)
				.toString();
	}


	public enum Status {
        NOT_POS("NOT_POS"),
        POS_OK("POS_OK"),
        WAIT("WAIT"),
        CBN("CBN"),
        CANCEL("CANCEL");

        private final String name;

        private Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
