package com.cisco.pricelists.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Created by Alf on 19.04.2014.
 */
@Entity(name = "pricelist")
public class Pricelist {

	private long id;
	private String partNumber;
	private String description;
	private int gpl;
	private double wpl;
	private double discount;

	public Pricelist(long id, String partNumber, String description, int gpl, double wpl, int discount) {
		this.id = id;
		this.partNumber = partNumber;
		this.description = description;
		this.gpl = gpl;
		this.wpl = wpl;
		this.discount = discount;
	}

	public Pricelist() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	@Column(name = "part_number")
	public String getPartNumber() {
		return partNumber;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	@Column(name = "gpl")
	public int getGpl() {
		return gpl;
	}

	@Column(name = "wpl")
	public double getWpl() {
		return wpl;
	}

	@Column(name = "discount")
	public double getDiscount() {
		return discount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGpl(int gpl) {
		this.gpl = gpl;
	}

	public void setWpl(double wpl) {
		this.wpl = wpl;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
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
		Pricelist rhs = (Pricelist) obj;
		return new EqualsBuilder()
				.append(this.id, rhs.id)
				.append(this.partNumber, rhs.partNumber)
				.append(this.description, rhs.description)
				.append(this.gpl, rhs.gpl)
				.append(this.wpl, rhs.wpl)
				.append(this.discount, rhs.discount)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(partNumber)
				.append(description)
				.append(gpl)
				.append(wpl)
				.append(discount)
				.toHashCode();
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("partNumber", partNumber)
				.append("description", description)
				.append("gpl", gpl)
				.append("wpl", wpl)
				.append("discount", discount)
				.toString();
	}
}
