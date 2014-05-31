package com.cisco.promos.excel;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 14:44
 */
@Component("promosImporter")
public class DefaultPromosImporter implements PromosImporter {

    @Autowired
    private PromosExtractor promosExtractor;

    @Autowired
    private PromosDao promosDao;

	@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void importPromos(InputStream inputStream) {

        List<Promo> promos = promosExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(promos)) {
            throw new CiscoException("Exported from excel promos are null or empty. Please, check file.");
        }

		Set<Promo> uniquePromos = Sets.newLinkedHashSet(promos);

        promosDao.deleteAll();
        promosDao.saveAll(Lists.newArrayList(uniquePromos));
    }
}
