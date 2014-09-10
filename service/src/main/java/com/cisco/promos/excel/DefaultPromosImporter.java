package com.cisco.promos.excel;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

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
    private PromosService promosService;

	@CacheEvict(value = "ciscoCache", key = "'promos'")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void importPromos(InputStream inputStream) {

        List<Promo> promos = promosExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(promos)) {
            throw new CiscoException("Exported from excel promos are null or empty. Please, check file.");
        }

        Set<Promo> uniquePromos = Sets.newLinkedHashSet(promos);

        promosService.deleteAll();
        promosService.saveAll(newArrayList(uniquePromos));
    }
}
