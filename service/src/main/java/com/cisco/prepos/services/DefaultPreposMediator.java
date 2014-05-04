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
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;

;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:39
 */

@Service
public class DefaultPreposMediator implements PreposMediator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PreposesDao preposesDao;

    @Autowired
    private SalesService salesService;

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private PricelistsService pricelistsService;

    @Autowired
    private PromosService promoService;

    @Autowired
    private DartsService dartsService;

    @Autowired
    private PreposModelConstructor preposModelConstructor;

    private Map<String, Client> clientsMap;
    private Map<String, Pricelist> pricelistsMap;
    private Map<String, Promo> promosMap;
    private Table<String, String, Dart> dartsTable;

    public void initRelatedTablesData() {

        clientsMap = clientsService.getClientsMap();
        pricelistsMap = pricelistsService.getPricelistsMap();
        promosMap = promoService.getPromosMap();
        dartsTable = dartsService.getDartsTable();
    }

    @Override
    public List<PreposModel> getNewPreposModels() {

        initRelatedTablesData();

        List<Sale> sales = salesService.getSales(NOT_PROCESSED);

        List<PreposModel> preposModels = preposModelConstructor.constructNewPreposModels(sales, clientsMap, pricelistsMap, promosMap, dartsTable);

        updateSalesTable(sales);

        return preposModels;
    }

    @Override
    public List<PreposModel> getAllPreposModels() {

        List<Prepos> allPreposes = preposesDao.getPreposes();

        List<PreposModel> preposModels = preposModelConstructor.constructPreposModels(allPreposes, dartsTable);

        return preposModels;
    }

    @Override
    public void save(List<PreposModel> preposModels) {

        List<Prepos> preposes = Lists.newArrayList();
        for (PreposModel model : preposModels) {
            preposes.add(model.getPrepos());
        }

        updateDartsTable(dartsTable);
        preposesDao.save(preposes);
    }

    @Override
    public void updatePreposDiscount(PreposModel preposModel) {

        preposModelConstructor.recountPreposDiscount(preposModel, pricelistsMap, promosMap, dartsTable);
    }

    private void updateSalesTable(List<Sale> sales) {
        salesService.update(sales);
    }

    private void updateDartsTable(Table<String, String, Dart> darts) {
        dartsService.update(darts.values());
    }

}
