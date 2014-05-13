package com.cisco.prepos.model;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartConstants;
import com.cisco.prepos.dto.Prepos;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.Timestamp;
import java.util.Iterator;
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

    //TODO Please, remove from model recounts and other stuff. We use model only for view. Now we can just remove recounting of darts according to discusses.
    public void setSelectedDart(Dart selectedDart) {

        if (this.selectedDart != selectedDart) {

            recountDartQuantity(selectedDart);
            this.prepos.setSecondPromo(selectedDart.getAuthorizationNumber());
            this.selectedDart = selectedDart;
        }
    }

    public void recountPrepos(double buyDiscount, double buyPrice) {
        prepos.setBuyDiscount(buyDiscount);
        prepos.setBuyPrice(buyPrice);
    }

    private void recountDartQuantity(Dart newDart) {

        if (this.selectedDart != DartConstants.EMPTY_DART) {
            this.selectedDart.setQuantity(this.selectedDart.getQuantity() + prepos.getQuantity());
        }
        if (newDart != DartConstants.EMPTY_DART) {
            newDart.setQuantity(newDart.getQuantity() - prepos.getQuantity());
            prepos.setSecondPromo(newDart.getAuthorizationNumber());
        }
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

    //TODO move it to separate entity PreposFilter. Why static? Why here? Use guava filters and provide tests.
    public static List<PreposModel> getFilteredPreposes(PreposRestrictions foodFilter, List<PreposModel> preposes) {
        List<PreposModel> filterredPreposes = Lists.newArrayList();
        String partnerName = foodFilter.getPartnerName().toLowerCase();
        String billNum = foodFilter.getShippedBillNumber().toLowerCase();
        Timestamp fromDate = foodFilter.getFromDate();
        Timestamp toDate = foodFilter.getToDate();

        for (Iterator<PreposModel> i = preposes.iterator(); i.hasNext(); ) {
            PreposModel tmp = i.next();
            if (tmp.getPrepos().getPartnerName().toLowerCase().contains(partnerName) &&
                    tmp.getPrepos().getShippedBillNumber().toLowerCase().contains(billNum) &&
                    tmp.getPrepos().getShippedDate().after(fromDate) &&
                    tmp.getPrepos().getShippedDate().before(toDate)) {
                filterredPreposes.add(tmp);
            }
        }

        return filterredPreposes;
    }

    //TODO Not sure that we need equals and hash code for model. But if we need - why only few fields included?
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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(prepos)
                .append(suitableDarts)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("prepos", prepos)
                .append("suitableDarts", suitableDarts)
                .toString();
    }
}
