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
	private PreposMediator preposMediator;

    private List<PreposModel> preposModels = Lists.newArrayList();

	@Transactional
    @Override
    public List<PreposModel> getAllData() {

	    List<PreposModel> newPreposModels = preposMediator.getNewPreposModels();
	    preposMediator.save(newPreposModels);

	    preposModels = preposMediator.getAllPreposModels();
        return preposModels;
    }

	@Override
	public void recountPrepos(PreposModel preposModel) {
		preposMediator.updatePreposDiscount(preposModel);
	}

    @Override
    public void save(List<PreposModel> prePos) {
		preposMediator.save(prePos);
    }

}
