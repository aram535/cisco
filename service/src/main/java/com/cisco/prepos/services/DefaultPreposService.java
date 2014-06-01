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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.cisco.prepos.dto.Prepos.Status;
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

    @Autowired
    private PreposValidator preposValidator;

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
    public void update(List<PreposModel> preposModels) {

        List<Prepos> preposes = preposModelConstructor.getPreposes(preposModels);
        preposesDao.update(preposes);
    }

    //TODO maybe db updates should be produced by sending events to needed services

	private List<Prepos> getPreposes(final Status... statuses) {

		List preposes = preposesDao.getPreposes();

		if (ArrayUtils.isEmpty(statuses)) {
			return Lists.newArrayList(preposes);
		}

		Collection<Prepos> filteredPreposes = Collections2.filter(preposes, new Predicate<Prepos>() {
			@Override
			public boolean apply(Prepos prepos) {
				Status status = prepos.getStatus();
				return ArrayUtils.contains(statuses, status);
			}
		});

		return Lists.newArrayList(filteredPreposes);
	}

    private void updateData(List<Sale> newSales, List<Prepos> newPreposes, List<Prepos> updatedPreposes) {
        preposesDao.update(updatedPreposes);
        preposesDao.save(newPreposes);
        salesService.updateSalesStatuses(newSales);
    }

}
