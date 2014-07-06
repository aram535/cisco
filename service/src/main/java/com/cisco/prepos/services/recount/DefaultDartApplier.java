package com.cisco.prepos.services.recount;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cisco.prepos.dto.PreposBuilder.builder;
import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getDiscountPart;
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

        PreposBuilder preposBuilder = builder().prepos(inputPrepos);

        String partNumber = inputPrepos.getPartNumber();
        int quantity = inputPrepos.getQuantity();
        double salePrice = inputPrepos.getSalePrice();
        long shippedDateInMillis = inputPrepos.getShippedDate().getTime();

        Promo firstPromo = promosMap.get(partNumber);
        Pricelist pricelist = pricelistsMap.get(partNumber);

        String newSecondPromo = selectedDart.getAuthorizationNumber();

        int gpl = discountProvider.getGpl(partNumber, pricelistsMap);
        double saleDiscount = getDiscountPart(salePrice, gpl);

        double buyDiscount = discountProvider.getDiscount(selectedDart, firstPromo, pricelist, shippedDateInMillis);

        double buyPrice = getBuyPrice(buyDiscount, gpl);
        double posSum = getRoundedDouble(buyPrice * quantity);
	    double detlaAsDouble = salePrice / buyPrice;
	    boolean isOk = detlaAsDouble > threshold;
		int delta = (int) ((detlaAsDouble * 100) - 100);

        if (firstPromo != null) {
            preposBuilder.firstPromo(firstPromo.getCode());
        } else {
	        preposBuilder.firstPromo(null);
        }

        preposBuilder.secondPromo(newSecondPromo);
		preposBuilder.endUser(selectedDart.getEndUserName());
        preposBuilder.saleDiscount(saleDiscount);
        preposBuilder.buyDiscount(buyDiscount);
        preposBuilder.buyPrice(buyPrice);
        preposBuilder.posSum(posSum);
        preposBuilder.ok(isOk);
	    preposBuilder.delta(delta);

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