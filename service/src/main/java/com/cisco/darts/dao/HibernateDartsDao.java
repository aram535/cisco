package com.cisco.darts.dao;

import com.cisco.darts.dto.Dart;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
@Repository
public class HibernateDartsDao implements DartsDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Dart> getDarts() {
		Session currentSession = sessionFactory.openSession();

		List<Dart> dartsList = currentSession.createCriteria(Dart.class).list();
		currentSession.close();
		return dartsList;
	}

	@Transactional
	@Override
	public void save(Dart dart) {
		Session currentSession = sessionFactory.openSession();
		Transaction transaction = currentSession.beginTransaction();
		currentSession.save(dart);
		transaction.commit();
		currentSession.close();
	}

	@Transactional
	@Override
	public void update(Dart dart) {
		Session currentSession = sessionFactory.openSession();
		Transaction transaction = currentSession.beginTransaction();
		currentSession.update(dart);
		transaction.commit();
		currentSession.close();
	}

	@Transactional
	@Override
	public void delete(Dart dart) {
		Session currentSession = sessionFactory.openSession();
		Transaction transaction = currentSession.beginTransaction();
		currentSession.delete(dart);
		transaction.commit();
		currentSession.close();
	}
}
