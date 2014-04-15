package com.cisco.clients.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 21:45
 */
@Entity(name = "client")
public class Client {

    private long id;
    private String clientNumber;
    private String name;
    private String city;
    private String address;

	public Client() {
	}

	public Client(long id, String clientNumber, String name, String city, String address) {
		this.id = id;
		this.clientNumber = clientNumber;
		this.name = name;
		this.city = city;
		this.address = address;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @Column(name = "client_number")
    public String getClientNumber() {
        return clientNumber;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setId(long id) {
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
        Client rhs = (Client) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.clientNumber, rhs.clientNumber)
                .append(this.name, rhs.name)
                .append(this.city, rhs.city)
                .append(this.address, rhs.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(clientNumber)
                .append(name)
                .append(city)
                .append(address)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
