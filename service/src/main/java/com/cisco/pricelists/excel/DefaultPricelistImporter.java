package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;

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
    private PricelistsDao pricelistsDao;


    @Override
    public void importPricelist(InputStream inputStream) {
        List<Pricelist> pricelist = pricelistExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(pricelist)) {
            throw new CiscoException("Exported from excel pricelist are null or empty. Please, check file.");
        }

        pricelistsDao.deleteAll();
        pricelistsDao.saveAll(pricelist);
    }
}
