package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
    private PricelistsService pricelistsService;

    @Autowired
    private PromosService promosService;

    @Autowired
    private ClientsService clientsService;

    @Override
    public List<PreposModel> getAllData() {

        List<Prepos> preposes = preposesDao.getPreposes();

        List<Prepos> updatedPreposes = preposUpdater.updatePreposes(preposes);

        preposesDao.updateAll(updatedPreposes);

        List<Sale> sales = salesService.getSales(NEW);

        Table<String, String, Dart> dartsTable = dartsService.getDartsTable();

        if (!CollectionUtils.isEmpty(sales)) {
            Map<String, Client> clientsMap = clientsService.getClientsMap();
            Map<String, Promo> promosMap = promosService.getPromosMap();
            Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();
            List<Prepos> newPreposes = preposConstructor.construct(sales, clientsMap, pricelistsMap, promosMap, dartsTable);
            preposesDao.save(newPreposes);
            updatedPreposes.addAll(newPreposes);
        }

        return preposModelConstructor.constructPreposModels(updatedPreposes, dartsTable);
    }

    @Override
    public void recountPrepos(PreposModel preposModel) {

    }

    @Override
    public void save(List<PreposModel> prePos) {

    }

}
