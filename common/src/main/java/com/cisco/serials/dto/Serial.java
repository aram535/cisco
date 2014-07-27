package com.cisco.serials.dto;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Created by Alf on 27.07.2014.
 */

@Entity(name = "serial")
public class Serial {

	private long id;
	private String serialNumber;

	public Serial() {
	}

	public Serial(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Serial(long id, String serialNumber) {
		this.id = id;
		this.serialNumber = serialNumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	@Column(name = "serial_number")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setId(long id) {
		this.id = id;
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
				.append(this.id, rhs.id)
				.append(this.serialNumber, rhs.serialNumber)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(serialNumber)
				.toHashCode();
	}


	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("id", id)
				.add("serialNumber", serialNumber)
				.toString();
	}
}
