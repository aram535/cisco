package com.cisco.accountmanager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 22:40
 */
public class AccountManagerModel {

    private long id;
    private String name;
    private List<String> partners;
    private List<String> endUsers;

    public AccountManagerModel(long id, String name, List<String> partners, List<String> endUsers) {
        this.id = id;
        this.name = name;
        this.partners = partners;
        this.endUsers = endUsers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPartners() {
        return partners;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }

    public List<String> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<String> endUsers) {
        this.endUsers = endUsers;
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
        AccountManagerModel rhs = (AccountManagerModel) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.name, rhs.name)
                .append(this.partners, rhs.partners)
                .append(this.endUsers, rhs.endUsers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(partners)
                .append(endUsers)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("partners", partners)
                .append("endUsers", endUsers)
                .toString();
    }
}
