package com.cisco.pricelists.excel;

import org.junit.Before;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:24
 */
public class DefaultPricelistExtractorTest extends BasePricelistExtractor {

    @Before
    public void init() {
        pricelistExtractor = new DefaultPricelistExtractor();
    }
}
