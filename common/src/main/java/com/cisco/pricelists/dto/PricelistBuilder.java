package com.cisco.pricelists.dto;

/**
 * Created by Alf on 19.04.2014.
 */
public class PricelistBuilder {
	private long id;
	private String partNumber;
	private String description;
	private Double gpl;
	private double wpl;
	private double discount;

	private PricelistBuilder() {
	}

	public static PricelistBuilder newPricelistBuilder() {
		return new PricelistBuilder();
	}

	public PricelistBuilder setId(long id) {
		this.id = id;
		return this;
	}

	public PricelistBuilder setPartNumber(String partNumber) {
		this.partNumber = partNumber;
		return this;
	}

	public PricelistBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public PricelistBuilder setGpl(Double gpl) {
		this.gpl = gpl;
		return this;
	}

	public PricelistBuilder setWpl(double wpl) {
		this.wpl = wpl;
		return this;
	}

	public PricelistBuilder setDiscount(double discount) {
		this.discount = discount;
		return this;
	}

	public Pricelist build() {
		Pricelist pricelist = new Pricelist();
		pricelist.setId(id);
		pricelist.setPartNumber(partNumber);
		pricelist.setDescription(description);
		pricelist.setGpl(gpl);
		pricelist.setWpl(wpl);
		pricelist.setDiscount(discount);
		return pricelist;
	}
}
