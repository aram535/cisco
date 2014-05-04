package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 03.05.2014.
 */
@PropertySource("classpath:prepos.properties")
public class DefaultPreposModelConstructor implements PreposModelConstructor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DiscountProvider discountProvider = new DefaultDiscountProvider();

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

            prepos.setStatus(Prepos.Status.NOT_PROCESSED);
            prepos.setClientNumber(sale.getClientNumber());
            prepos.setShippedDate(sale.getShippedDate());
            prepos.setShippedBillNumber(sale.getShippedBillNumber());
            prepos.setComment(sale.getComment());
            prepos.setSerials(sale.getSerials());
            prepos.setZip(sale.getClientZip());
            prepos.setType(sale.getCiscoType());
            prepos.setQuantity(sale.getQuantity());
            prepos.setPartNumber(sale.getPartNumber());
            prepos.setSalePrice(sale.getPrice());

            assignPartnerName(sale, prepos, clientsMap);

            Promo firstPromo = promosMap.get(prepos.getPartNumber());
            if (firstPromo != null) {
                prepos.setFirstPromo(firstPromo.getCode());
            }

            assignSecondPromo(prepos, preposModel, dartsTable);

            double gpl = getGpl(prepos, pricelistsMap);

            assignSaleDiscount(prepos, gpl);
            assignBuyDiscount(prepos, pricelistsMap, promosMap, dartsTable);
            assignBuyPrice(prepos, gpl);
            assignOk(prepos, threshold);

            preposModel.setPrepos(prepos);
            preposes.add(preposModel);

            sale.setStatus(Sale.Status.PROCESSED);
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

        double buyDiscount = discountProvider.getDiscount(preposModel.getPrepos(), dartsTable, promosMap, pricelistsMap);

        preposModel.getPrepos().setBuyDiscount(buyDiscount);

        Pricelist pricelist = pricelistsMap.get(preposModel.getPrepos().getPartNumber());

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale part number %s not found", preposModel.getPrepos().getPartNumber()));
        }

        double gpl = pricelist.getGpl();

        double buyPrice = (double) Math.round(gpl * (1 - preposModel.getPrepos().getBuyDiscount()) * 100) / 100;

        preposModel.getPrepos().setBuyPrice(buyPrice);

        if ((preposModel.getPrepos().getSalePrice() / preposModel.getPrepos().getBuyPrice()) > threshold) {
            preposModel.getPrepos().setOk(true);
        } else {
            preposModel.getPrepos().setOk(false);
        }
    }

    private void assignOk(Prepos prepos, double threshold) {

        if ((prepos.getSalePrice() / prepos.getBuyPrice()) > threshold) {
            prepos.setOk(true);
        } else {
            prepos.setOk(false);
        }
    }

    private void assignBuyPrice(Prepos prepos, double gpl) {

        double buyPrice = (double) Math.round(gpl * (1 - prepos.getBuyDiscount()) * 100) / 100;

        prepos.setBuyPrice(buyPrice);
    }

    private void assignBuyDiscount(Prepos prepos, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable) {

        double buyDiscount = discountProvider.getDiscount(prepos, dartsTable, promosMap, pricelistsMap);

        prepos.setBuyDiscount(buyDiscount);
    }

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
            preposModel.setSelectedDart(PreposModel.emptyDart);
        }
        preposModel.setSuitableDarts(suitableDarts);

    }

    private void assignSaleDiscount(Prepos prepos, double gpl) {

        double saleDiscount = (double) Math.round((1 - (prepos.getSalePrice() / gpl)) * 100) / 100;

        prepos.setSaleDiscount(saleDiscount);
    }

    private void assignPartnerName(Sale sale, Prepos prepos, Map<String, Client> clientsMap) {
        Client client = clientsMap.get(sale.getClientNumber());
        if (client != null) {
            prepos.setPartnerName(client.getName());
        } else {
            prepos.setPartnerName(sale.getClientName());
        }
    }

    private double getGpl(Prepos prepos, Map<String, Pricelist> pricelistsMap) {
        Pricelist pricelist = pricelistsMap.get(prepos.getPartNumber());

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale part number %s not found", prepos.getPartNumber()));
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
