package com.cisco.promos.excel;

import com.cisco.exception.CiscoException;
import com.cisco.promos.dao.PromosDao;
import com.cisco.promos.dto.Promo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;

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


    @Override
    public void importPromos(InputStream inputStream) {

        List<Promo> promos = promosExtractor.extract(inputStream);

        if (CollectionUtils.isEmpty(promos)) {
            throw new CiscoException("Exported from excel promos are null or empty. Please, check file.");
        }

        promosDao.deleteAll();
        promosDao.saveAll(promos);
    }
}
