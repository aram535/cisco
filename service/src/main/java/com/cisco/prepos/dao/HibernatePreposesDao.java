package com.cisco.prepos.dao;

import com.cisco.prepos.dto.Prepos;
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
 * Created by Alf on 05.04.14.
 */
@Repository
public class HibernatePreposesDao implements PreposesDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Prepos> getPreposes() {
        Session currentSession = sessionFactory.getCurrentSession();
        List<Prepos> preposesList = currentSession.createCriteria(Prepos.class).list();

	    logger.info("{} preposes fetched from DB", preposesList.size());
        return preposesList;
    }

	@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Prepos prepos) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(prepos);
    }

	@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(List<Prepos> preposList) {
        Session currentSession = sessionFactory.getCurrentSession();
        for (Prepos prepos : preposList) {
            currentSession.save(prepos);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Prepos prepos) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(prepos);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(List<Prepos> preposList) {
        Session currentSession = sessionFactory.getCurrentSession();
        for (Prepos prepos : preposList) {
            currentSession.update(prepos);
        }
    }


	@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Prepos prepos) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(prepos);
    }
}
