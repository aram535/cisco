package com.cisco.prepos.model;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 26.04.2014.
 */
public class PreposModel {

    private Prepos prepos;
    private Dart selectedDart;
    private boolean checked = false;

    private Map<String, Dart> suitableDarts;

    public PreposModel(Prepos prepos, Map<String, Dart> suitableDarts, Dart selectedDart) {
        this.prepos = prepos;
        this.suitableDarts = suitableDarts;
        this.selectedDart = selectedDart;
    }

    public Prepos getPrepos() {
        return prepos;
    }

    public Dart getSelectedDart() {
        return selectedDart;
    }

    public Map<String, Dart> getSuitableDarts() {
        return suitableDarts;
    }

    public List<Dart> getSuitableDartsList() {
        List<Dart> dartList = Lists.newArrayList(suitableDarts.values());
        return dartList;
    }

    public boolean getChecked() {
        return checked;
    }

    public int getBuyDiscount() {
        return (int) (prepos.getBuyDiscount() * 100);
    }

    public int getSaleDiscount() {
        return (int) (prepos.getSaleDiscount() * 100);
    }

    public void setSelectedDart(Dart selectedDart) {
        this.selectedDart = selectedDart;
    }

    public void setPrepos(Prepos prepos) {
        this.prepos = prepos;
    }

    public void setSuitableDarts(Map<String, Dart> suitableDarts) {
        this.suitableDarts = suitableDarts;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
        PreposModel rhs = (PreposModel) obj;
        return new EqualsBuilder()
                .append(this.prepos, rhs.prepos)
                .append(this.suitableDarts, rhs.suitableDarts)
                .append(this.selectedDart, rhs.selectedDart)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(prepos)
                .append(suitableDarts)
                .append(selectedDart)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("prepos", prepos)
                .append("suitableDarts", suitableDarts)
                .append("selectedDart", selectedDart)
                .toString();
    }
}
