package com.cisco.pricelists.excel;

import org.junit.Before;

/**
 * User: Rost
 * Date: 07.07.2014
 * Time: 20:00
 */
public class EventDrivenPricelistExtractorTest extends BasePricelistExtractor {

    @Before
    public void init() {
        pricelistExtractor = new EventDrivenPricelistExtractor();
    }
}
