package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
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

                double gpl = getGpl(partNumber, pricelistsMap);
                double saleDiscount = getSaleDiscount(salePrice, gpl);
                preposBuilder.saleDiscount(saleDiscount);

                Prepos prepos = preposBuilder.build();
                return new Triplet(prepos, suitableDarts, selectedDart);
            }
        }));

        return recountedPreposes;
    }

    //TODO have a sense to make smth like SaleDiscountProvider with method 'getSaleDiscount' or put it to DiscountProvider
    private double getGpl(String partNumber, Map<String, Pricelist> pricelistsMap) {

        Pricelist pricelist = pricelistsMap.get(partNumber);

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale part number %s not found", partNumber));
        }

        return pricelist.getGpl();
    }

    private double getSaleDiscount(double price, double gpl) {

        double saleDiscount = (double) Math.round((1 - (price / gpl)) * 100) / 100;

        return saleDiscount;
    }
}
