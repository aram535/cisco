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
    private int saleDiscount;
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

	public Prepos() {
	}

	Prepos(long id, String type, String partnerName, String partNumber, double posSum, int quantity, boolean ok, int delta, int saleDiscount, double buyDiscount, double salePrice, double buyPrice, String firstPromo, String secondPromo, String endUser, String clientNumber, Timestamp shippedDate, String shippedBillNumber, String comment, String serials, int zip) {
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
	public int getSaleDiscount() {
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

    public void setSaleDiscount(int saleDiscount) {
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
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
