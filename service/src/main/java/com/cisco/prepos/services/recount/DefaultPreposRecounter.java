package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.promo.PromoValidator;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
@Component
public class DefaultPreposRecounter implements PreposRecounter {

    @Autowired
    private DartApplier dartApplier;

    @Autowired
    private DartsService dartsService;

    @Autowired
    private PricelistsService pricelistsService;

    @Autowired
    private PromosService promosService;

    @Autowired
    private SuitableDartsProvider suitableDartsProvider;

    @Autowired
    private DartSelector dartSelector;

    @Autowired
    private PromoValidator promoValidator;


    @Override
    public List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recount(List<Prepos> preposes) {
        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }
        final Table<String, String, Dart> dartsTable = dartsService.getDartsTable();
        final Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();
        final Map<String, Promo> promosMap = promosService.getPromosMap();
        List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recountedPreposes = Lists.newArrayList(Lists.transform(preposes, new Function<Prepos, Quartet<Prepos, Map<String, Dart>, Dart, Boolean>>() {
            @Override
            public Quartet<Prepos, Map<String, Dart>, Dart, Boolean> apply(Prepos inputPrepos) {

                String partNumber = inputPrepos.getPartNumber();
                String partnerName = inputPrepos.getPartnerName();
                int quantity = inputPrepos.getQuantity();
                String secondPromo = inputPrepos.getSecondPromo();
                Timestamp shippedDate = inputPrepos.getShippedDate();

                Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(partNumber, partnerName, quantity, shippedDate, dartsTable);
                Dart selectedDart = dartSelector.selectDart(suitableDarts, secondPromo);

                Promo firstPromo = promosMap.get(partNumber);
                boolean firstPromoValid = promoValidator.isValid(firstPromo, shippedDate.getTime());

                Prepos prepos = dartApplier.getPrepos(inputPrepos, selectedDart, pricelistsMap, dartsTable, promosMap);
                return new Quartet(prepos, suitableDarts, selectedDart, firstPromoValid);
            }
        }));

        return recountedPreposes;
    }

}
