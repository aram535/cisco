package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status.NOT_PROCESSED;
import static com.cisco.prepos.model.PreposModel.EMPTY_DART;
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

    @Autowired
    private SuitableDartsProvider suitableDartsProvider;

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
            int quantity = sale.getQuantity();
            String partnerName = partnerNameProvider.getPartnerName(sale, clientsMap);
            Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(partNumber, partnerName, quantity, dartsTable);
            Dart firstSuitableDart = getFirstSuitableDart(suitableDarts);
            String firstPromo = getFirstPromo(promosMap, partNumber);
            String secondPromo = firstSuitableDart.getAuthorizationNumber();
            double gpl = getGpl(partNumber, pricelistsMap);
            double salePrice = sale.getPrice();
            double saleDiscount = getSaleDiscount(salePrice, gpl);

            Triplet<String, String, String> discountInfo = new Triplet<>(partNumber, firstPromo, secondPromo);
            double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);
            double buyPrice = getBuyPrice(buyDiscount, gpl);
            boolean okStatus = getOkStatus(salePrice, buyPrice, threshold);

            prepos.setPartNumber(partNumber);
            prepos.setPartnerName(partnerName);
            prepos.setStatus(NOT_PROCESSED);
            prepos.setClientNumber(sale.getClientNumber());
            prepos.setShippedDate(sale.getShippedDate());
            prepos.setShippedBillNumber(sale.getShippedBillNumber());
            prepos.setComment(sale.getComment());
            prepos.setSerials(sale.getSerials());
            prepos.setZip(sale.getClientZip());
            prepos.setType(sale.getCiscoType());
            prepos.setQuantity(quantity);
            prepos.setSalePrice(salePrice);
            prepos.setFirstPromo(firstPromo);
            prepos.setSecondPromo(secondPromo);
            prepos.setEndUser(firstSuitableDart.getEndUserName());
            prepos.setSaleDiscount(saleDiscount);
            prepos.setBuyDiscount(buyDiscount);
            prepos.setSecondPromo(secondPromo);
            prepos.setBuyPrice(buyPrice);
            prepos.setOk(okStatus);

            preposModel.setPrepos(prepos);
            preposModel.setSelectedDart(firstSuitableDart);
            //TODO just hook. Should be refactored
            if (firstSuitableDart != EMPTY_DART) {
                firstSuitableDart.setQuantity(firstSuitableDart.getQuantity() - quantity);
            }

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

    private String getFirstPromo(Map<String, Promo> promosMap, String partNumber) {
        Promo firstPromo = promosMap.get(partNumber);
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

    private Dart getFirstSuitableDart(Map<String, Dart> suitableDarts) {
        Collection<Dart> darts = suitableDarts.values();
        Iterator<Dart> dartIterator = darts.iterator();
        if (dartIterator.hasNext()) {
            Dart dart = dartIterator.next();
            return dart;
        }
        return EMPTY_DART;
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

        Prepos prepos = preposModel.getPrepos();
        int quantity = prepos.getQuantity();
        String partnerName = prepos.getPartnerName();
        String partNumber = prepos.getPartNumber();
        String secondPromo = prepos.getSecondPromo();

        Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(partNumber, partnerName, quantity, dartsTable);
        Dart firstSuitableDart = getFirstSuitableDart(suitableDarts);
        if (secondPromo == null) {
            prepos.setEndUser(firstSuitableDart.getEndUserName());
            preposModel.setSelectedDart(firstSuitableDart);
            //TODO just hook. Should be refactored
            if (firstSuitableDart != EMPTY_DART) {
                firstSuitableDart.setQuantity(firstSuitableDart.getQuantity() - quantity);
            }
        } else {
            Dart dart = dartsTable.get(partNumber, secondPromo);
            preposModel.setSelectedDart(dart);
            preposModel.setSuitableDarts(suitableDarts);
        }
    }
}
