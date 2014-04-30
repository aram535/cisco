package com.cisco.prepos.model;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 26.04.2014.
 */
public class PreposModel {

	private Prepos prepos;

	private Map<String, Dart> suitableDarts;
	private Dart selectedPromo;

	public PreposModel() {
	}

	public PreposModel(Prepos prepos, Map<String, Dart> suitableDarts) {
		this.prepos = prepos;
		this.suitableDarts = suitableDarts;
	}

	public Prepos getPrepos() {
		return prepos;
	}

	public Dart getSelectedPromo() {
		return selectedPromo;
	}

	public Map<String, Dart> getSuitableDarts() {
		return suitableDarts;
	}

	public List<Dart> getSuitableDartsList() {
		ArrayList<Dart> suitableDarts = new ArrayList<Dart>(this.suitableDarts.values());
		return suitableDarts;
	}

	public void setSelectedPromo(Dart selectedPromo) {

		if(this.selectedPromo == null) {

			this.selectedPromo = selectedPromo;

		} else if(this.selectedPromo != selectedPromo) {

			recountDartQuantity(selectedPromo);
			this.prepos.setSecondPromo(selectedPromo.getAuthorizationNumber());
			this.selectedPromo = selectedPromo;
		}


	}

	public void recountPrepos(double buyDiscount, double buyPrice) {
		prepos.setBuyDiscount(buyDiscount);
		prepos.setBuyPrice(buyPrice);
	}

	private void recountDartQuantity(Dart newDart) {
		this.selectedPromo.setQuantity(this.selectedPromo.getQuantity() + prepos.getQuantity());

		selectedPromo.setQuantity(selectedPromo.getQuantity() - prepos.getQuantity());
		prepos.setSecondPromo(selectedPromo.getAuthorizationNumber());
	}


	public void setPrepos(Prepos prepos) {
		this.prepos = prepos;
	}

	public void setSuitableDarts(Map<String, Dart> suitableDarts) {
		this.suitableDarts = suitableDarts;
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
