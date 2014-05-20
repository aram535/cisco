package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.DartApplier;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.cisco.sales.dto.Sale.Status.NEW;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public class DefaultPreposService implements PreposService {

    @Autowired
    private PreposesDao preposesDao;

    @Autowired
    private SalesService salesService;

    @Autowired
    private DartsService dartsService;

    @Autowired
    private PreposConstructor preposConstructor;

    @Autowired
    private PreposModelConstructor preposModelConstructor;

    @Autowired
    private PreposUpdater preposUpdater;

    @Autowired
    private DartApplier dartApplier;

    @Autowired
    private PricelistsService pricelistsService;

    @Autowired
    private PromosService promosService;

    @Override
    public List<PreposModel> getAllData() {

        List<Prepos> preposes = preposesDao.getPreposes();
        List<Prepos> updatedPreposes = preposUpdater.update(preposes);

        List<Sale> newSales = salesService.getSales(NEW);
        List<Prepos> newPreposes = getNewPreposes(newSales);

        updateData(newSales, newPreposes, updatedPreposes);

        updatedPreposes.addAll(newPreposes);
        return preposModelConstructor.construct(updatedPreposes);
    }

    @Override
    public Prepos recountPrepos(Prepos prepos, Dart selectedDart) {
        return dartApplier.getPrepos(prepos, selectedDart, pricelistsService.getPricelistsMap(), dartsService.getDartsTable(), promosService.getPromosMap());
    }

    @Transactional
    @Override
    public void update(List<PreposModel> preposModels) {
        List<Prepos> preposes = preposModelConstructor.getPreposesFromPreposModels(preposModels);
        preposesDao.update(preposes);
        dartsService.update(dartsService.getDarts());
    }

    private List<Prepos> getNewPreposes(List<Sale> sales) {

        if (CollectionUtils.isEmpty(sales)) {
            return Lists.newArrayList();
        }

        List<Prepos> newPreposes = preposConstructor.construct(sales);
        return newPreposes;
    }

    //TODO maybe db updates should be produced by sending events to needed services
    @Transactional
    private void updateData(List<Sale> newSales, List<Prepos> newPreposes, List<Prepos> updatedPreposes) {
        preposesDao.update(updatedPreposes);
        preposesDao.save(newPreposes);
        salesService.updateSalesStatuses(newSales);
    }

}
