package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.discount.utils.DiscountPartCounter;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;

@Component
@PropertySource("classpath:db.properties")
public class DefaultDartApplier implements DartApplier {

    @Autowired
    private DiscountProvider discountProvider;

    @Value("${good.threshold}")
    private double threshold;

    @Override
    public Prepos getPrepos(Prepos inputPrepos, Dart selectedDart, Map<String, Pricelist> pricelistsMap, Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap) {

        PreposBuilder preposBuilder = PreposBuilder.builder().prepos(inputPrepos);

        String partNumber = inputPrepos.getPartNumber();
        int quantity = inputPrepos.getQuantity();
        double salePrice = inputPrepos.getSalePrice();
        String firstPromo = inputPrepos.getFirstPromo();

        String newSecondPromo = selectedDart.getAuthorizationNumber();

        int gpl = discountProvider.getGpl(partNumber, pricelistsMap);
        double saleDiscount = DiscountPartCounter.getDiscountPart(salePrice, gpl);


        Triplet<String, String, String> discountInfo = new Triplet(partNumber, firstPromo, newSecondPromo);
        double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);
        double buyPrice = getBuyPrice(buyDiscount, gpl);
        double posSum = DiscountPartCounter.getRoundedDouble(buyPrice * quantity);
        boolean isOk = (salePrice / buyPrice) > threshold;

        preposBuilder.secondPromo(newSecondPromo);
        preposBuilder.endUser(selectedDart.getEndUserName());
        preposBuilder.saleDiscount(saleDiscount);
        preposBuilder.buyDiscount(buyDiscount);
        preposBuilder.buyPrice(buyPrice);
        preposBuilder.posSum(posSum);
        preposBuilder.ok(isOk);

        Prepos prepos = preposBuilder.build();
        return prepos;
    }

    void setThreshold(double threshold) {
        this.threshold = threshold;
    }


    private double getBuyPrice(double buyDiscount, double gpl) {

        double buyPrice = getRoundedDouble(gpl * (1 - buyDiscount));

        return buyPrice;
    }
}