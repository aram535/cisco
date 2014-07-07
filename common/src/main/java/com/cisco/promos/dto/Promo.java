package com.cisco.promos.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Alf on 19.04.2014.
 */

@Entity(name = "promo")
public class Promo {

	private long id;
	private String partNumber;
	private String description;
	private double discount;
	private String name;
	private double gpl;
	private String code;
	private double claimPerUnit;
	private int version;
	private Timestamp endDate;

	public Promo(long id, String partNumber, String description, double discount, String name, double gpl, String code, double claimPerUnit, int version, Timestamp endDate) {
		this.id = id;
		this.partNumber = partNumber;
		this.description = description;
		this.discount = discount;
		this.name = name;
		this.gpl = gpl;
		this.code = code;
		this.claimPerUnit = claimPerUnit;
		this.version = version;
		this.endDate = endDate;
	}

	public Promo() {
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

	@Column(name = "discount")
	public double getDiscount() {
		return discount;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Column(name = "gpl")
	public double getGpl() {
		return gpl;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	@Column(name = "claim_per_unit")
	public double getClaimPerUnit() {
		return claimPerUnit;
	}

	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	@Column(name = "end_date")
	public Timestamp getEndDate() {
		return endDate;
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

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGpl(double gpl) {
		this.gpl = gpl;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setClaimPerUnit(double claimPerUnit) {
		this.claimPerUnit = claimPerUnit;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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
		Promo rhs = (Promo) obj;
		return new EqualsBuilder()
				.append(this.id, rhs.id)
				.append(this.partNumber, rhs.partNumber)
				.append(this.description, rhs.description)
				.append(this.discount, rhs.discount)
				.append(this.name, rhs.name)
				.append(this.gpl, rhs.gpl)
				.append(this.code, rhs.code)
				.append(this.claimPerUnit, rhs.claimPerUnit)
				.append(this.version, rhs.version)
				.append(this.endDate, rhs.endDate)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(partNumber)
				.append(description)
				.append(discount)
				.append(name)
				.append(gpl)
				.append(code)
				.append(claimPerUnit)
				.append(version)
				.append(endDate)
				.toHashCode();
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("partNumber", partNumber)
				.append("description", description)
				.append("discount", discount)
				.append("name", name)
				.append("gpl", gpl)
				.append("code", code)
				.append("claimPerUnit", claimPerUnit)
				.append("version", version)
				.append("endDate", endDate)
				.toString();
	}
}
