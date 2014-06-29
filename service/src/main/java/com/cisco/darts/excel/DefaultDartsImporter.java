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

		    List<Dart> dartsToDelete = findUnrelevantDartsAndRecountNewOnes(existingDartsTable, newDartsTable);
		    if(!dartsToDelete.isEmpty()) {
			    dartsService.delete(dartsToDelete);
		    }
	    }

	    dartsService.saveAll(Lists.newArrayList(uniqueDarts));
    }

	private List<Dart> findUnrelevantDartsAndRecountNewOnes(Table<String, String, Dart> existingDartsTable, Table<String, String, Dart> newDartsTable) {
		List<Dart> dartsToDelete = Lists.newLinkedList();

		for (String authNumber : newDartsTable.columnKeySet()) {
			Map<String, Dart> newDartsMap = newDartsTable.column(authNumber);
			Map<String, Dart> existingDartsMap = existingDartsTable.column(authNumber);

			if(existingDartsMap != null) {
				for (Dart newDart : newDartsMap.values()) {
					Dart exisitingDart = existingDartsMap.get(newDart.getCiscoSku());
					if(exisitingDart != null && newDart.getVersion() > exisitingDart.getVersion()) {
						//recount new dart quantity respecting quantity of corresponding dart with older version
						int quantity = exisitingDart.getQuantity() + (newDart.getQuantityInitial() - exisitingDart.getQuantityInitial());
						newDart.setQuantity(quantity);
					}
				}
			}
			dartsToDelete.addAll(existingDartsMap.values());
		}

		return dartsToDelete;
	}

}
