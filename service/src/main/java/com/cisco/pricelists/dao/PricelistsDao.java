package com.cisco.pricelists.dao;

import com.cisco.pricelists.dto.Pricelist;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
public interface PricelistsDao {

    List<Pricelist> getPricelists();

    void save(Pricelist pricelist);

    void update(Pricelist pricelist);

    void delete(Pricelist pricelist);

    int deleteAll();

    void saveAll(Iterable<Pricelist> expectedPricelist);
}
