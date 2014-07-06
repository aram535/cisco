package com.cisco.claims.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Alf on 05.07.2014.
 */
public class Claim {

	private String partNumber;
	private String shippedBillNumber;
	private long claimId;
	private long batchId;

	public Claim(String partNumber, String shippedBillNumber, long claimId, long batchId) {
		this.partNumber = partNumber;
		this.shippedBillNumber = shippedBillNumber;
		this.claimId = claimId;
		this.batchId = batchId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public String getShippedBillNumber() {
		return shippedBillNumber;
	}

	public long getClaimId() {
		return claimId;
	}

	public long getBatchId() {
		return batchId;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setShippedBillNumber(String shippedBillNumber) {
		this.shippedBillNumber = shippedBillNumber;
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
		Claim rhs = (Claim) obj;
		return new EqualsBuilder()
				.append(this.partNumber, rhs.partNumber)
				.append(this.shippedBillNumber, rhs.shippedBillNumber)
				.append(this.claimId, rhs.claimId)
				.append(this.batchId, rhs.batchId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(partNumber)
				.append(shippedBillNumber)
				.append(claimId)
				.append(batchId)
				.toHashCode();
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("partNumber", partNumber)
				.append("shippedBillNumber", shippedBillNumber)
				.append("claimId", claimId)
				.append("batchId", batchId)
				.toString();
	}
}
