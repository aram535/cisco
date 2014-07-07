package com.cisco.promos.dto;

import java.sql.Timestamp;

/**
 * Created by Alf on 19.04.2014.
 */
public class PromoBuilder {
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

	private PromoBuilder() {
	}

	public static PromoBuilder newPromoBuilder() {
		return new PromoBuilder();
	}

	public PromoBuilder setId(long id) {
		this.id = id;
		return this;
	}

	public PromoBuilder setPartNumber(String partNumber) {
		this.partNumber = partNumber;
		return this;
	}

	public PromoBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public PromoBuilder setDiscount(double discount) {
		this.discount = discount;
		return this;
	}

	public PromoBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public PromoBuilder setGpl(double gpl) {
		this.gpl = gpl;
		return this;
	}

	public PromoBuilder setCode(String code) {
		this.code = code;
		return this;
	}

	public PromoBuilder setClaimPerUnit(double claimPerUnit) {
		this.claimPerUnit = claimPerUnit;
		return this;
	}

	public PromoBuilder setVersion(int version) {
		this.version = version;
		return this;
	}

	public PromoBuilder setEndDate(Timestamp endDate) {
		this.endDate = endDate;
		return this;
	}

	public Promo build() {
		Promo promo = new Promo();
		promo.setId(id);
		promo.setPartNumber(partNumber);
		promo.setDescription(description);
		promo.setDiscount(discount);
		promo.setName(name);
		promo.setGpl(gpl);
		promo.setCode(code);
		promo.setClaimPerUnit(claimPerUnit);
		promo.setVersion(version);
		promo.setEndDate(endDate);
		return promo;
	}
}
