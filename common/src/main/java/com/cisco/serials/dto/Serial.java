package com.cisco.serials.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Alf on 27.07.2014.
 */

@Entity(name = "serial")
public class Serial {

	private String serialNumber;

	public Serial() {
	}

	public Serial(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Id
	@Column(name = "serial_number")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
		Serial rhs = (Serial) obj;
		return new EqualsBuilder()
				.append(this.serialNumber, rhs.serialNumber)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(serialNumber)
				.toHashCode();
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("serialNumber", serialNumber)
				.toString();
	}
}
