package com.cisco.prepos.services;

import com.cisco.prepos.dto.Prepos;
import com.cisco.sales.dto.Sale;

import java.util.List;

/**
 * Created by Alf on 03.05.2014.
 */
public interface PreposConstructor {

    List<Prepos> construct(List<Sale> sales);

}
