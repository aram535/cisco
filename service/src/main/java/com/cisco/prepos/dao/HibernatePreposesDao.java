package com.cisco.prepos.dao;

import com.cisco.prepos.dto.Prepos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Alf on 05.04.14.
 */
@Repository
public class HibernatePreposesDao implements PreposesDao {


	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Prepos> getPreposes() {
		Session currentSession = sessionFactory.getCurrentSession();
		List<Prepos> preposesList = currentSession.createCriteria(Prepos.class).list();
		return preposesList;
	}

	@Transactional
	@Override
	public void save(Prepos prepos) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.save(prepos);
	}

	@Transactional
	@Override
	public void save(List<Prepos> preposList) {
		Session currentSession = sessionFactory.getCurrentSession();
		for (Prepos prepos : preposList) {
			currentSession.save(prepos);
		}
	}

	@Transactional
	@Override
	public void update(Prepos prepos) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(prepos);
	}



	@Transactional
	@Override
	public void delete(Prepos prepos) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.delete(prepos);
	}
}
