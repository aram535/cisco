package com.cisco.pricelists.excel;

import com.cisco.pricelists.dto.Pricelist;

import java.io.InputStream;
import java.util.Map;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:23
 */
public interface PricelistExtractor {

    public Map<String, Pricelist> extract(InputStream file);

}
