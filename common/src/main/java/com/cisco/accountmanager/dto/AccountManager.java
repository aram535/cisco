package com.cisco.accountmanager.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 21:32
 */
@Entity(name = "account_manager")
public class AccountManager {

    private long id;
    private String name;
    private String jsonPartners;
    private String jsonEndUsers;

    public AccountManager() {
    }

    public AccountManager(long id, String name, String jsonPartners, String jsonEndUsers) {
        this.id = id;
        this.name = name;
        this.jsonPartners = jsonPartners;
        this.jsonEndUsers = jsonEndUsers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "json_partners")
    public String getJsonPartners() {
        return jsonPartners;
    }

    @Column(name = "json_end_users")
    public String getJsonEndUsers() {
        return jsonEndUsers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setJsonPartners(String jsonPartners) {
        this.jsonPartners = jsonPartners;
    }

    public void setJsonEndUsers(String jsonEndUsers) {
        this.jsonEndUsers = jsonEndUsers;
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
        AccountManager rhs = (AccountManager) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.name, rhs.name)
                .append(this.jsonPartners, rhs.jsonPartners)
                .append(this.jsonEndUsers, rhs.jsonEndUsers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(jsonPartners)
                .append(jsonEndUsers)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("jsonPartners", jsonPartners)
                .append("jsonEndUsers", jsonEndUsers)
                .toString();
    }
}
