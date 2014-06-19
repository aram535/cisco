package com.cisco.darts.dao;

import com.cisco.darts.dto.Dart;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Alf on 15.04.14.
 */
@Repository
public class HibernateDartsDao implements DartsDao {

	private static final String DELETE_ALL_HQL = String.format("delete from %s", Dart.class.getSimpleName().toLowerCase());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Dart> getDarts() {
        Session currentSession = sessionFactory.getCurrentSession();
        List<Dart> dartsList = currentSession.createCriteria(Dart.class).list();
        return dartsList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Dart dart) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(dart);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Dart dart) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(dart);
    }

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void update(Collection<Dart> darts) {
		Session currentSession = sessionFactory.getCurrentSession();
		for (Dart dart : darts) {
			currentSession.update(dart);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Dart dart) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(dart);
    }

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int deleteAll() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery(DELETE_ALL_HQL);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveAll(List<Dart> darts) {
		Session currentSession = sessionFactory.getCurrentSession();
		for (Dart dart : darts) {
			currentSession.saveOrUpdate(dart);
		}
	}
}
