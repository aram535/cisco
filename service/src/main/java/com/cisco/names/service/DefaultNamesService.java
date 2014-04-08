package com.cisco.names.service;

import com.cisco.names.dao.NamesDao;
import com.cisco.names.dto.Names;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alf on 08.04.14.
 */
@Service("namesService")
public class DefaultNamesService implements NamesService {

    @Autowired
    NamesDao namesDao;

	private List<Names> serviceData = initSomeData();

    @Override
    public List<Names> getAllData() {
        //return namesDao.getAll();

	    return serviceData;
    }

    @Override
    public void save(Names name) {
	    serviceData.add(name);
    }

    @Override
    public void update(Names nameList) {

    }

	@Override
	public void delete(Names selectedNamesModel) {

	}


	public void setNamesDao(NamesDao namesDao) {
        this.namesDao = namesDao;
    }

	private List<Names> initSomeData() {
		Names name1 = new Names("1", "331", "SPEZVUZAUTOMATIKA", "KHARKOV", "str. Princess Olga 102/43");
		Names name2 = new Names("2", "332", "SPEZVUZAUTOMATIKA", "KIEV", "str. Geroev Kosmosa 18");
		Names name3 = new Names("3", "333", "SPEZVUZAUTOMATIKA", "ODESSA", "str. Dyuka 3a");

		return Lists.newArrayList(name1, name2, name3);
	}
}
