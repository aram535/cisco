package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;

import java.util.List;

import static com.cisco.prepos.dto.Prepos.Status;

/**
 * Created by Alf on 05.04.14.
 */
public interface PreposService {

    public List<PreposModel> getAllData(final Status... status);

    public void update(List<PreposModel> prePos);

    public Prepos recountPrepos(Prepos prepos, Dart selectedDart);

	void validatePreposForSelectedDart(List<PreposModel> preposModels, PreposModel preposModel);
}
