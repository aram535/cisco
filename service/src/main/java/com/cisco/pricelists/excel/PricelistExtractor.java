package com.cisco.pricelists.excel;

import com.cisco.pricelists.dto.Pricelist;

import java.io.InputStream;
import java.util.List;

/**
 * User: Rost
 * Date: 29.04.2014
 * Time: 21:23
 */
public interface PricelistExtractor {

    public List<Pricelist> extract(InputStream file);

}
