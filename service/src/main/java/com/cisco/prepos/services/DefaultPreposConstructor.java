package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.dto.PreposBuilder;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;

;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:39
 */
public class DefaultPreposConstructor implements PreposConstructor {

    @Autowired
    private SalesService salesService;

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private PricelistsService pricelistsService;

    @Override
    public List<Prepos> getPreposes() {

        List<Sale> sales = salesService.getSales(NOT_PROCESSED);

        if (CollectionUtils.isEmpty(sales)) {
            return Lists.newArrayList();
        }

        Map<String, Client> clientsMap = clientsService.getClientsMap();

        Map<String, Pricelist> pricelistsMap = pricelistsService.getPricelistsMap();

        List<Prepos> preposes = Lists.newArrayList();

        for (Sale sale : sales) {

            PreposBuilder builder = PreposBuilder.builder();

            assignType(sale, builder);

            assignPartnerName(clientsMap, sale, builder);

            assignPartNumber(sale, builder);

            assignQuantity(sale, builder);

            double salePrice = sale.getPrice();

            assignSalePrice(salePrice, builder);

            assignSaleDiscount(pricelistsMap, sale, builder, salePrice);

            Prepos prepos = builder.build();

            preposes.add(prepos);
        }

        return preposes;
    }

    private void assignSaleDiscount(Map<String, Pricelist> pricelistsMap, Sale sale, PreposBuilder builder, double salePrice) {

        Pricelist pricelist = pricelistsMap.get(sale.getPartNumber());

        if (pricelist == null) {
            throw new CiscoException(String.format("price for sale %s not found", sale));
        }

        int gpl = pricelist.getGpl();

        int saleDiscount = 100 - (int) Math.round((salePrice / gpl) * 100);

        builder.saleDiscount(saleDiscount);
    }

    private void assignSalePrice(double salePrice, PreposBuilder builder) {
        builder.salePrice(salePrice);
    }

    private void assignQuantity(Sale sale, PreposBuilder builder) {
        builder.quantity(sale.getQuantity());
    }

    private void assignPartNumber(Sale sale, PreposBuilder builder) {
        builder.partNumber(sale.getPartNumber());
    }

    private void assignType(Sale sale, PreposBuilder builder) {
        builder.type(sale.getCiscoType());
    }

    private void assignPartnerName(Map<String, Client> clientsMap, Sale sale, PreposBuilder builder) {
        Client client = clientsMap.get(sale.getClientNumber());
        if (client != null) {
            builder.partnerName(client.getName());
        } else {
            builder.partnerName(sale.getClientName());
        }
    }
}
