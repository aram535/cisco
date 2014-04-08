package com.cisco.names.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Alf on 08.04.14.
 */
public class Names {

    private String id;
    private String clientNumber;
    private String name;
    private String city;
    private String address;

	public Names() {
	}

	public Names(String id, String clientNumber, String name, String city, String address) {
        this.id = id;
        this.clientNumber = clientNumber;
        this.name = name;
        this.city = city;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
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
        Names rhs = (Names) obj;
        return new EqualsBuilder()
                .append(this.clientNumber, rhs.clientNumber)
                .append(this.name, rhs.name)
                .append(this.city, rhs.city)
                .append(this.address, rhs.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(clientNumber)
                .append(name)
                .append(city)
                .append(address)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientNumber", clientNumber)
                .append("name", name)
                .append("city", city)
                .append("address", address)
                .toString();
    }
}