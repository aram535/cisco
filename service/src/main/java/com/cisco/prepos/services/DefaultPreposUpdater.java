package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * User: Rost
 * Date: 09.05.2014
 * Time: 19:04
 */
@Component
public class DefaultPreposUpdater implements PreposUpdater {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SalesService salesService;

    @Override
    public List<Prepos> update(final List<Prepos> preposes) {

        final Table<String, String, Sale> salesTable = salesService.getSalesTable();

        Collection<Prepos> updatedPreposes = Collections2.transform(preposes, new Function<Prepos, Prepos>() {
            @Override
            public Prepos apply(Prepos prepos) {
                String serials = prepos.getSerials();
                if (StringUtils.isBlank(serials)) {
                    String partNumber = prepos.getPartNumber();
                    String shippedBillNumber = prepos.getShippedBillNumber();
                    Sale sale = salesTable.get(partNumber, shippedBillNumber);
                    if (sale != null) {
                        prepos.setSerials(sale.getSerials());
                    } else {
                        logger.debug("No sale was found for prepos {}", prepos);
                    }
                }
                return prepos;
            }
        });

        return Lists.newArrayList(updatedPreposes);
    }
}
