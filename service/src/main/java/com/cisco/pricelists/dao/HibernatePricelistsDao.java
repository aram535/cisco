package com.cisco.pricelists.dao;

import com.cisco.pricelists.dto.Pricelist;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alf on 19.04.2014.
 */
@Repository
public class HibernatePricelistsDao implements PricelistsDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Pricelist> getPricelists() {
		Session currentSession = sessionFactory.getCurrentSession();
		return currentSession.createCriteria(Pricelist.class).list();
	}

	@Transactional
	@Override
	public void save(Pricelist pricelist) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.save(pricelist);
	}

	@Transactional
	@Override
	public void update(Pricelist pricelist) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(pricelist);
	}

	@Transactional
	@Override
	public void delete(Pricelist pricelist) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.delete(pricelist);
	}
}
