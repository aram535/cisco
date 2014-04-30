package com.cisco.pricelists.excel;

import java.io.InputStream;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 18:28
 */
public interface PricelistImporter {

    void importPricelist(InputStream inputStream);

}
