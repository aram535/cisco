package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.services.partner.PartnerNameProvider;
import com.cisco.sales.dto.Sale;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.cisco.prepos.dto.Prepos.Status.NOT_PROCESSED;

/**
 * Created by Alf on 03.05.2014.
 */
@Component
@PropertySource("classpath:prepos.properties")
public class DefaultPreposConstructor implements PreposConstructor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PartnerNameProvider partnerNameProvider;

    @Autowired
    private ClientsService clientsService;

    @Override
    public List<Prepos> construct(List<Sale> sales) {

        Map<String, Client> clientsMap = clientsService.getClientsMap();

        List<Prepos> preposes = Lists.newArrayList();

        for (Sale sale : sales) {

            Prepos prepos = new Prepos();

            prepos.setSalePrice(sale.getPrice());
            prepos.setPartNumber(sale.getPartNumber());
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

            preposes.add(prepos);
        }

        logger.debug(String.format("%d new sales processed", sales.size()));

        return preposes;
    }
}
