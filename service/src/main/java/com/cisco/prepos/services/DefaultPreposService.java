package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.posready.service.PosreadyService;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.DartApplier;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status;
import static com.cisco.sales.dto.Sale.Status.NEW;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Created by Alf on 05.04.14.
 */
@Service("preposService")
public final class DefaultPreposService implements PreposService {

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

    @Autowired
    private PreposValidator preposValidator;

	@Autowired
	private PosreadyService posreadyService;

	@Autowired
	private ClientsService clientsService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<PreposModel> getAllData(final Status... statuses) {

        List<Prepos> preposes = getPreposes(statuses);
        List<Prepos> updatedPreposes = preposUpdater.update(preposes);

        List<Sale> newSales = salesService.getSales(NEW);
        List<Prepos> newPreposes = preposConstructor.construct(newSales);

        updateData(newSales, newPreposes, updatedPreposes);

        updatedPreposes.addAll(newPreposes);
        return preposModelConstructor.construct(updatedPreposes);
    }

    @Override
    public Prepos recountPrepos(Prepos prepos, Dart selectedDart) {
        return dartApplier.getPrepos(prepos, selectedDart, pricelistsService.getPricelistsMap(), dartsService.getDartsTable(), promosService.getPromosMap());
    }

    @Override
    public void validatePreposForSelectedDart(List<PreposModel> preposModels, PreposModel preposModel) {
        preposValidator.validateDartQuantity(preposModels, preposModel);
    }

    @Override
    public void updateFromModels(List<PreposModel> preposModels) {

        List<Prepos> preposes = preposModelConstructor.getPreposes(preposModels);
        preposesDao.update(preposes);
    }

	@Override
	public void update(List<Prepos> preposes) {
		preposesDao.update(preposes);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String exportPosready(Collection<PreposModel> preposModels) {

		if(preposModels.isEmpty()) {
			throw new CiscoException("Nothing to export. Preposes list is empty");
		}

		for (PreposModel preposModel : preposModels) {
			if(preposModel.getPrepos().getStatus() != Status.NOT_PROCESSED) {
				throw new CiscoException("All preposes should be in " + Prepos.Status.NOT_PROCESSED.toString() + " status");
			}
		}

		List<Prepos> preposes = preposModelConstructor.getPreposes(preposModels);

		Map<String, Client> clientsMap = clientsService.getClientsMap();
		Table<String, String, Dart> dartsTable = dartsService.getDartsTable();
		Map<String, Promo> promosMap = promosService.getPromosMap();
		Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();

		String path = posreadyService.exportPosready(preposes, clientsMap, pricelistsMap, dartsTable, promosMap);

		String posreadyId = FilenameUtils.getBaseName(path).replace(posreadyService.posreadyFilePrefix, "");

		for (Prepos prepos : preposes) {
			prepos.setPosreadyId(posreadyId);
			prepos.setStatus(Status.WAITING);
		}

		preposesDao.update(preposes);

		return path;
	}

	//TODO maybe db updates should be produced by sending events to needed services

	@Override
    public List<Prepos> getPreposes(final Status... statuses) {

        List preposes = preposesDao.getPreposes();

        if (isEmpty(statuses)) {
            return newArrayList(preposes);
        }

        Collection<Prepos> filteredPreposes = Collections2.filter(preposes, new Predicate<Prepos>() {
            @Override
            public boolean apply(Prepos prepos) {
                Status status = prepos.getStatus();
                return contains(statuses, status);
            }
        });

        return newArrayList(filteredPreposes);
    }

    private void updateData(List<Sale> newSales, List<Prepos> newPreposes, List<Prepos> updatedPreposes) {
        preposesDao.update(updatedPreposes);
        preposesDao.save(newPreposes);
        salesService.updateSalesStatuses(newSales);
    }

}
