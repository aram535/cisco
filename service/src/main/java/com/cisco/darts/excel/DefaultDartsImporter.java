package com.cisco.darts.excel;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartAssistant;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alf on 20.05.2014.
 */
@Component("dartsImporter")
public class DefaultDartsImporter implements DartsImporter {

	@Autowired
	private DartsExtractor dartsExtractor;

	@Autowired
	private DartsService dartsService;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void importDarts(InputStream inputStream) {

		List<Dart> darts = dartsExtractor.extract(inputStream);

		if (CollectionUtils.isEmpty(darts)) {
			throw new CiscoException("Exported from excel dart are null or empty. Please, check file.");
		}

		Set<Dart> uniqueDarts = Sets.newLinkedHashSet(darts);

		Table<String, String, Dart> existingDartsTable = dartsService.getDartsTable();
		Table<String, String, Dart> newDartsTable = DartAssistant.dartsToTable(uniqueDarts);

		if (existingDartsTable != null) {

			List<Dart> dartsToUpdate = Lists.newArrayList();
			List<Dart> dartsToDelete = findUnrelevantDartsAndRecountNewOnes(existingDartsTable, newDartsTable, uniqueDarts, dartsToUpdate);
			if(!dartsToUpdate.isEmpty()) {
				dartsService.update(dartsToUpdate);
			}
			if(!dartsToDelete.isEmpty()) {
				dartsService.delete(dartsToDelete);
			}

		}

		dartsService.saveAll(Lists.newArrayList(uniqueDarts));
	}

	private List<Dart> findUnrelevantDartsAndRecountNewOnes(Table<String, String, Dart> existingDartsTable,
	                                                        Table<String, String, Dart> newDartsTable,
	                                                        Set<Dart> newDarts, List<Dart> dartsToUpdate) {
		List<Dart> dartsToDelete = Lists.newLinkedList();

		for (String authNumber : newDartsTable.columnKeySet()) {
			Map<String, Dart> newDartsMap = newDartsTable.column(authNumber);
			Map<String, Dart> existingDartsMap = existingDartsTable.column(authNumber);

			if(existingDartsMap != null) {
				for (Dart newDart : newDartsMap.values()) {
					Dart existingDart = existingDartsMap.get(newDart.getCiscoSku());
					if(existingDart != null) {
						if(newDart.getVersion() > existingDart.getVersion()) {
							//recount new dart quantity respecting quantity of corresponding dart with older version
							int quantity = existingDart.getQuantity() + (newDart.getQuantityInitial() - existingDart.getQuantityInitial());
							newDart.setQuantity(quantity);
							existingDart.copyFrom(newDart);
							dartsToUpdate.add(existingDart);
							existingDartsMap.remove(existingDart.getCiscoSku());
							newDarts.remove(newDart);
						} else {
							throw new CiscoException(String.format("Existing darts has same or higher VERSION(%s) then new one VERSION(%s)! (CISKO_SKU=%s, AUTHORITHATION_NUMBER=%s",
									existingDart.getVersion(), newDart.getVersion(), existingDart.getCiscoSku(), existingDart.getAuthorizationNumber()));
						}
					}
				}
				dartsToDelete.addAll(existingDartsMap.values());
			}
		}

		return dartsToDelete;
	}

}
