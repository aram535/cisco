package com.cisco.prepos.model;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 26.04.2014.
 */
public class PreposModel {

	public static final Dart EMPTY_DART = new Dart("", "");

	private Prepos prepos;
	private Dart selectedDart = EMPTY_DART;

	private Map<String, Dart> suitableDarts = Maps.newHashMap();
	{
		suitableDarts.put("", EMPTY_DART);
	}

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

	    if(this.selectedDart != EMPTY_DART) {
		    this.selectedDart.setQuantity(this.selectedDart.getQuantity() + prepos.getQuantity());
	    }
        if (newDart != EMPTY_DART) {
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


    public static List<PreposModel> getFilteredPreposes(PreposFilter foodFilter, List<PreposModel> preposes) {
        List<PreposModel> filterredPreposes = Lists.newArrayList();
        String partnerName = foodFilter.getPartnerName().toLowerCase();
        String billNum = foodFilter.getShippedBillNumber().toLowerCase();

        for (Iterator<PreposModel> i = preposes.iterator(); i.hasNext(); ) {
            PreposModel tmp = i.next();
            if (tmp.getPrepos().getPartnerName().toLowerCase().contains(partnerName) &&
                    tmp.getPrepos().getShippedBillNumber().toLowerCase().contains(billNum)) {
                filterredPreposes.add(tmp);
            }
        }

        return filterredPreposes;
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
