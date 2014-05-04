package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status.NOT_PROCESSED;
import static com.cisco.sales.dto.Sale.Status.PROCESSED;

/**
 * Created by Alf on 03.05.2014.
 */
@Component
@PropertySource("classpath:prepos.properties")
public class DefaultPreposModelConstructor implements PreposModelConstructor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiscountProvider discountProvider;

    @Autowired
    private PartnerNameProvider partnerNameProvider;

    @Value("${good.threshold}")
    private double threshold;

    @Override
    public List<PreposModel> constructNewPreposModels(List<Sale> sales, Map<String, Client> clientsMap, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable) {

        if (CollectionUtils.isEmpty(sales)) {
            return Lists.newArrayList();
        }

        List<PreposModel> preposes = Lists.newArrayList();

        for (Sale sale : sales) {

            PreposModel preposModel = new PreposModel();
            Prepos prepos = new Prepos();

            String partNumber = sale.getPartNumber();
            double gpl = getGpl(partNumber, pricelistsMap);

            prepos.setPartNumber(partNumber);
            prepos.setPartnerName(partnerNameProvider.getPartnerName(sale, clientsMap));
            prepos.setStatus(NOT_PROCESSED);
            prepos.setClientNumber(sale.getClientNumber());
            prepos.setShippedDate(sale.getShippedDate());
            prepos.setShippedBillNumber(sale.getShippedBillNumber());
            prepos.setComment(sale.getComment());
            prepos.setSerials(sale.getSerials());
            prepos.setZip(sale.getClientZip());
            prepos.setType(sale.getCiscoType());
            prepos.setQuantity(sale.getQuantity());
            prepos.setSalePrice(sale.getPrice());

            String firstPromo = getFirstPromo(promosMap, prepos);
            prepos.setFirstPromo(firstPromo);

            assignSecondPromo(prepos, preposModel, dartsTable);


            double saleDiscount = getSaleDiscount(prepos.getSalePrice(), gpl);
            prepos.setSaleDiscount(saleDiscount);


            double buyDiscount = discountProvider.getDiscount(getDiscountInfo(prepos), dartsTable, promosMap, pricelistsMap);
            prepos.setBuyDiscount(buyDiscount);

            double buyPrice = getBuyPrice(prepos.getBuyDiscount(), gpl);
            prepos.setBuyPrice(buyPrice);

            boolean okStatus = getOkStatus(prepos.getSalePrice(), prepos.getBuyPrice(), threshold);
            prepos.setOk(okStatus);

            preposModel.setPrepos(prepos);
            preposes.add(preposModel);

            sale.setStatus(PROCESSED);
        }

        logger.debug(String.format("%d new sales processed", sales.size()));

        return preposes;
    }

    @Override
    public List<PreposModel> constructPreposModels(List<Prepos> preposes, Table<String, String, Dart> dartsTable) {

        List<PreposModel> preposModels = Lists.newArrayList();

        for (Prepos prepos : preposes) {

            PreposModel preposModel = new PreposModel();
            preposModel.setPrepos(prepos);
            setSuitableDarts(preposModel, dartsTable);

            preposModels.add(preposModel);
        }

        return preposModels;
    }

    @Override
    public void recountPreposDiscount(PreposModel preposModel, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable) {

        Prepos prepos = preposModel.getPrepos();
        Triplet<String, String, String> discountInfo = getDiscountInfo(prepos);

        double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);

        prepos.setBuyDiscount(buyDiscount);

        Pricelist pricelist = pricelistsMap.get(preposModel.getPrepos().getPartNumber());

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale part number %s not found", preposModel.getPrepos().getPartNumber()));
        }

        double gpl = pricelist.getGpl();

        double buyPrice = (double) Math.round(gpl * (1 - preposModel.getPrepos().getBuyDiscount()) * 100) / 100;

        prepos.setBuyPrice(buyPrice);

        boolean okStatus = getOkStatus(prepos.getSalePrice(), prepos.getBuyPrice(), threshold);
        prepos.setOk(okStatus);
    }

    private Triplet<String, String, String> getDiscountInfo(Prepos prepos) {
        return new Triplet<>(prepos.getPartNumber(), prepos.getFirstPromo(), prepos.getSecondPromo());
    }

    private String getFirstPromo(Map<String, Promo> promosMap, Prepos prepos) {
        Promo firstPromo = promosMap.get(prepos.getPartNumber());
        if (firstPromo != null) {
            return firstPromo.getCode();
        }
        return null;
    }

    private boolean getOkStatus(double salePrice, double buyPrice, double threshold) {

        if ((salePrice / buyPrice) > threshold) {
            return true;
        }
        return false;
    }

    private double getBuyPrice(double buyDiscount, double gpl) {

        double buyPrice = (double) Math.round(gpl * (1 - buyDiscount) * 100) / 100;

        return buyPrice;
    }

    //TODO refactoring
    private void assignSecondPromo(Prepos prepos, PreposModel preposModel, Table<String, String, Dart> dartsTable) {

        Map<String, Dart> suitableDarts = Maps.newHashMap();

        Map<String, Dart> suitableAuthNumsDarts = dartsTable.row(prepos.getPartNumber());
        for (Dart dart : suitableAuthNumsDarts.values()) {
            if (prepos.getPartnerName().equals(dart.getResellerName()) && dart.getQuantity() >= prepos.getQuantity()) {
                suitableDarts.put(dart.getAuthorizationNumber(), dart);

                if (prepos.getSecondPromo() == null) {
                    prepos.setSecondPromo(dart.getAuthorizationNumber());
                    dart.setQuantity(dart.getQuantity() - prepos.getQuantity());
                    preposModel.setSelectedDart(dart);
                    prepos.setEndUser(dart.getEndUserName());
                }
            }
        }

        if (preposModel.getSelectedDart() == null) {
            preposModel.setSelectedDart(PreposModel.EMPTY_DART);
        }
        preposModel.setSuitableDarts(suitableDarts);
    }

    private double getSaleDiscount(double price, double gpl) {

        double saleDiscount = (double) Math.round((1 - (price / gpl)) * 100) / 100;

        return saleDiscount;
    }

    private double getGpl(String partNumber, Map<String, Pricelist> pricelistsMap) {
        Pricelist pricelist = pricelistsMap.get(partNumber);

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale part number %s not found", partNumber));
        }

        return pricelist.getGpl();
    }

    private void setSuitableDarts(PreposModel preposModel, Table<String, String, Dart> dartsTable) {

        if (preposModel.getPrepos().getSecondPromo() == null) {
            assignSecondPromo(preposModel.getPrepos(), preposModel, dartsTable);
        } else {
            Map<String, Dart> suitableDarts = Maps.newHashMap();
            Map<String, Dart> suitableAuthNumsDarts = dartsTable.row(preposModel.getPrepos().getPartNumber());

            preposModel.setSelectedDart(suitableAuthNumsDarts.get(preposModel.getPrepos().getSecondPromo()));

            for (Dart dart : suitableAuthNumsDarts.values()) {
                if (preposModel.getPrepos().getPartnerName().equals(dart.getResellerName())
                        && dart.getQuantity() >= preposModel.getPrepos().getQuantity()) {
                    suitableDarts.put(dart.getAuthorizationNumber(), dart);
                }
            }

            preposModel.setSuitableDarts(suitableDarts);
        }
    }
}
