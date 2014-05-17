package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.dart.DartSelector;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.discount.utils.DiscountPartCounter;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.PreposBuilder.builder;
import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getDiscountPart;
import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;

/**
 * User: Rost
 * Date: 13.05.2014
 * Time: 23:11
 */
@PropertySource("classpath:db.properties")
@Component
public class DefaultPreposRecounter implements PreposRecounter {

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
    private DiscountProvider discountProvider;

    @Value("${good.threshold}")
    private double threshold;

    @Override
    public List<Triplet<Prepos, Map<String, Dart>, Dart>> recount(List<Prepos> preposes) {
        if (CollectionUtils.isEmpty(preposes)) {
            return Lists.newArrayList();
        }
        final Table<String, String, Dart> dartsTable = dartsService.getDartsTable();
        final Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();
        final Map<String, Promo> promosMap = promosService.getPromosMap();
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
                String firstPromo = input.getFirstPromo();

                Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(partNumber, partnerName, quantity, shippedDate, dartsTable);
                Dart selectedDart = dartSelector.selectDart(suitableDarts, secondPromo);

                String newSecondPromo = selectedDart.getAuthorizationNumber();
                preposBuilder.secondPromo(newSecondPromo);
                preposBuilder.endUser(selectedDart.getEndUserName());

                int gpl = discountProvider.getGpl(partNumber, pricelistsMap);
                double saleDiscount = getDiscountPart(salePrice, gpl);
                preposBuilder.saleDiscount(saleDiscount);


                Triplet<String, String, String> discountInfo = new Triplet(partNumber, firstPromo, newSecondPromo);
                double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);
                preposBuilder.buyDiscount(buyDiscount);
                double buyPrice = getBuyPrice(buyDiscount, gpl);
                preposBuilder.buyPrice(buyPrice);
                double posSum = getRoundedDouble(buyPrice * quantity);
                preposBuilder.posSum(posSum);
                boolean isOk = (salePrice / buyPrice) > threshold;
                preposBuilder.ok(isOk);

                Prepos prepos = preposBuilder.build();
                return new Triplet(prepos, suitableDarts, selectedDart);
            }
        }));

        return recountedPreposes;
    }

    private double getBuyPrice(double buyDiscount, double gpl) {

        double buyPrice = getRoundedDouble(gpl * (1 - buyDiscount));

        return buyPrice;
    }

    void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
