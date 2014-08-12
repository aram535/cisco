package com.cisco.pricelists.dao;

import com.cisco.pricelists.dto.Pricelist;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
@Repository
public class HibernatePricelistsDao implements PricelistsDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DELETE_ALL_HQL = String.format("delete from %s", Pricelist.class.getSimpleName().toLowerCase());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Pricelist> getPricelists() {
        Session currentSession = sessionFactory.getCurrentSession();
	    List pricelistList = currentSession.createCriteria(Pricelist.class).list();

	    logger.info("{} pricelists fetched from DB", pricelistList.size());
	    return pricelistList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Pricelist pricelist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(pricelist);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAll(Iterable<Pricelist> pricelist) {
        Session currentSession = sessionFactory.getCurrentSession();
        for (Pricelist price : pricelist) {
            currentSession.save(price);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Pricelist pricelist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(pricelist);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Pricelist pricelist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(pricelist);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery(DELETE_ALL_HQL);
        return query.executeUpdate();
    }
}
