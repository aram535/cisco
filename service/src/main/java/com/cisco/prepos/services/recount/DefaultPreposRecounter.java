package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.PreposBuilder.builder;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
public class DefaultPreposRecounter implements PreposRecounter {

    @Autowired
    private DartsService dartsService;

    @Autowired
    private SuitableDartsProvider suitableDartsProvider;

    @Autowired
    private DartSelector dartSelector;

    @Autowired
    private PricelistsService pricelistsService;

    @Autowired
    private DiscountProvider discountProvider;

    @Override
    public List<Triplet<Prepos, Map<String, Dart>, Dart>> recount(List<Prepos> preposes) {
        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }
        final Table<String, String, Dart> dartsTable = dartsService.getDartsTable();
        final Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();
        List<Triplet<Prepos, Map<String, Dart>, Dart>> recountedPreposes = Lists.newArrayList(Lists.transform(preposes, new Function<Prepos, Triplet<Prepos, Map<String, Dart>, Dart>>() {
            @Override
            public Triplet<Prepos, Map<String, Dart>, Dart> apply(Prepos input) {
                PreposBuilder preposBuilder = builder().prepos(input);
                String partNumber = input.getPartNumber();
                String partnerName = input.getPartnerName();
                int quantity = input.getQuantity();
                String secondPromo = input.getSecondPromo();
                Timestamp shippedDate = input.getShippedDate();
                double salePrice = input.getSalePrice();

                Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(partNumber, partnerName, quantity, shippedDate, dartsTable);
                Dart selectedDart = dartSelector.selectDart(suitableDarts, secondPromo);

                preposBuilder.secondPromo(selectedDart.getAuthorizationNumber());
                preposBuilder.endUser(selectedDart.getEndUserName());

                double saleDiscount = discountProvider.getSaleDiscount(partNumber, pricelistsMap, salePrice);
                preposBuilder.saleDiscount(saleDiscount);

                Prepos prepos = preposBuilder.build();
                return new Triplet(prepos, suitableDarts, selectedDart);
            }
        }));

        return recountedPreposes;
    }
}
