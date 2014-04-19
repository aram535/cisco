package com.cisco.promos.dao;

import com.cisco.promos.dto.Promo;
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
public class HibernatePromosDao implements PromosDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Promo> getPromos() {
		Session currentSession = sessionFactory.getCurrentSession();

		return currentSession.createCriteria(Promo.class).list();
	}

	@Transactional
	@Override
	public void save(Promo promo) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.save(promo);
	}

	@Transactional
	@Override
	public void update(Promo promo) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.update(promo);
	}

	@Transactional
	@Override
	public void delete(Promo promo) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.delete(promo);
	}
}
