package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<Prepos> updatePreposes(List<Prepos> preposes) {

	    Table<String, String, Sale> salesTable = salesService.getSalesTable();

	    for (Prepos prepos : preposes) {

		    Sale sale = salesTable.get(prepos.getPartNumber(), prepos.getShippedBillNumber());

		    if(sale != null) {
			    prepos.setSerials(sale.getSerials());
		    } else {
			    logger.warn("No sale was found for prepos", prepos);
		    }

	    }

	    return preposes;
    }
}
