package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;

import java.util.Collection;
import java.util.List;

import static com.cisco.prepos.dto.Prepos.Status;

/**
 * Created by Alf on 05.04.14.
 */
public interface PreposService {

    public List<PreposModel> getAllData(final Status... status);

    public void updateFromModels(List<PreposModel> preposModels);

    public void update(List<Prepos> preposes);

    public Prepos recountPrepos(Prepos prepos, Dart selectedDart);

	void validatePreposForSelectedDart(List<PreposModel> preposModels, PreposModel preposModel);

	public String exportPosready(Collection<PreposModel> preposes);

	public List<Prepos> getPreposes(final Status... statuses);
}
