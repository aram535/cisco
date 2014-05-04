package com.cisco.darts.service;

import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
@Service("dartsService")
public class DefaultDartsService implements DartsService {

    @Autowired
    private DartsDao dartsDao;

    @Override
    public List<Dart> getDarts() {
        return dartsDao.getDarts();
    }

    @Override
    public void save(Dart dart) {
        dartsDao.save(dart);
    }

    @Override
    public void update(Dart dart) {
        dartsDao.update(dart);
    }

    @Override
    public void update(Collection<Dart> darts) {
        dartsDao.update(darts);
    }

    @Override
    public void delete(Dart dart) {
        dartsDao.delete(dart);
    }

    @Override
    public Table<String, String, Dart> getDartsTable() {
        List<Dart> darts = dartsDao.getDarts();

        return dartsListToTable(darts);
    }

    private Table<String, String, Dart> dartsListToTable(List<Dart> darts) {

        HashBasedTable<String, String, Dart> dartsTable = HashBasedTable.create();

        for (Dart dart : darts) {
            dartsTable.put(dart.getCiscoSku(), dart.getAuthorizationNumber(), dart);
        }

        return dartsTable;
    }

}
