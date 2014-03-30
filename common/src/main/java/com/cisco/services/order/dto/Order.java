package com.cisco.services.order.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Rost
 * Date: 28.03.14
 * Time: 21:05
 */
public class Order {
    private final long id;
    private final String name;
    private final double amount;

    public Order(long id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
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
        Order rhs = (Order) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.name, rhs.name)
                .append(this.amount, rhs.amount)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(amount)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
