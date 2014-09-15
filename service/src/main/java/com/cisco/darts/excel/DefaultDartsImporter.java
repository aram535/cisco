package com.cisco.darts.excel;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.dto.DartBuilder;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static com.cisco.darts.dto.DartAssistant.dartsToTable;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.springframework.util.CollectionUtils.isEmpty;

;

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

        if (isEmpty(darts)) {
            throw new CiscoException("Exported from excel dart are null or empty. Please, check file.");
        }

        Set<Dart> uniqueDarts = newHashSet(darts);

        Table<String, String, Dart> existingDartsTable = dartsService.getDartsTable();
        Table<String, String, Dart> newDartsTable = dartsToTable(uniqueDarts);

        Pair<List<Dart>, List<Dart>> recountedAndNewDarts = recountedAndNewDarts(existingDartsTable, newDartsTable);
        dartsService.update(recountedAndNewDarts.getLeft());
        dartsService.saveAll(recountedAndNewDarts.getRight());
    }

    private Pair<List<Dart>, List<Dart>> recountedAndNewDarts(Table<String, String, Dart> existingDartsTable, Table<String, String, Dart> newDartsTable) {
        List<Dart> recountedDarts = Lists.newArrayList();
        List<Dart> newDarts = Lists.newArrayList();
        for (Dart newDart : newDartsTable.values()) {
            Dart existingDart = existingDartsTable.get(newDart.getCiscoSku(), newDart.getAuthorizationNumber());
            if (existingDart == null) {
                newDarts.add(newDart);
            } else if (shouldBeRecounted(existingDart, newDart)) {
                Dart recountedDart = recountDart(existingDart, newDart);
                recountedDarts.add(recountedDart);
            }
        }
        return of(recountedDarts, newDarts);
    }

    private Dart recountDart(Dart existingDart, Dart newDart) {
        int initialQuantityDelta = newDart.getQuantityInitial() - existingDart.getQuantityInitial();
        int quantity = existingDart.getQuantity() + initialQuantityDelta;
        Dart recountedDart = DartBuilder.builder(newDart).setQuantity(quantity).build();
        return recountedDart;
    }

    private boolean shouldBeRecounted(Dart existingDart, Dart newDart) {
        return newDart.getVersion() > existingDart.getVersion();
    }

}
