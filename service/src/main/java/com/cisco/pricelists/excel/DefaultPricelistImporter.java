package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 18:29
 */
@Component("pricelistImporter")
public class DefaultPricelistImporter implements PricelistImporter {

    @Autowired
    private PricelistExtractor pricelistExtractor;

    @Autowired
    private PricelistsService pricelistsService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void importPricelist(InputStream inputStream) {
        Map<String, Pricelist> pricelistMap = pricelistExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(pricelistMap)) {
            throw new CiscoException("Exported from excel pricelist are null or empty. Please, check file.");
        }

        pricelistsService.deleteAll();
        pricelistsService.saveAll(newArrayList(pricelistMap.values()));
    }
}
