package com.cisco.pricelists.excel;

import com.cisco.exception.CiscoException;
import com.cisco.pricelists.dao.PricelistsDao;
import com.cisco.pricelists.dto.Pricelist;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

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

	@Transactional
    @Override
    public void importPricelist(InputStream inputStream) {
        List<Pricelist> pricelist = pricelistExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(pricelist)) {
            throw new CiscoException("Exported from excel pricelist are null or empty. Please, check file.");
        }

		Set<Pricelist> uniquePricelists = Sets.newLinkedHashSet(pricelist);

        pricelistsDao.deleteAll();
        pricelistsDao.saveAll(Lists.newArrayList(uniquePricelists));
    }
}
