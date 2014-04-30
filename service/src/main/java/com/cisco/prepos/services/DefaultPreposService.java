package com.cisco.prepos.services;

import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public class DefaultPreposService implements PreposService {

	@Autowired
	private PreposConstructor preposConstructor;

    private List<PreposModel> preposModels = Lists.newArrayList();

	@Transactional
    @Override
    public List<PreposModel> getAllData() {

	    List<PreposModel> newPreposModels = preposConstructor.getNewPreposModels();
	    preposConstructor.save(newPreposModels);

	    preposModels = preposConstructor.getAllPreposModels();
        return preposModels;
    }

	@Override
	public void recountPrepos(PreposModel preposModel) {

		preposConstructor.recountPreposForNewPromo(preposModel);
	}

    @Override
    public void save(List<PreposModel> prePos) {
		preposConstructor.save(prePos);
    }

}
