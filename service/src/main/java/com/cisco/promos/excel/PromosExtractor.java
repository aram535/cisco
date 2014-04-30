package com.cisco.promos.excel;

import com.cisco.promos.dto.Promo;

import java.io.InputStream;
import java.util.List;

/**
 * User: Rost
 * Date: 30.04.2014
 * Time: 0:14
 */
public interface PromosExtractor {

    List<Promo> extract(InputStream file);

}
