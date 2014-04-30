package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public class DefaultPreposService implements PreposService {

	@Autowired
	private PreposConstructor preposConstructor;

    private List<PreposModel> preposModels = Lists.newArrayList();

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
    public void save(Prepos prePos) {

    }

    @Override
    public void update(List<Prepos> prePosList) {

    }

	@PostConstruct
    public void init() {

    }

}
