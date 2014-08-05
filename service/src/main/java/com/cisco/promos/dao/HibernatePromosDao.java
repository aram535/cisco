package com.cisco.promos.dao;

import com.cisco.promos.dto.Promo;
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

@Repository
public class HibernatePromosDao implements PromosDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DELETE_ALL_HQL = String.format("delete from %s", Promo.class.getSimpleName().toLowerCase());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Promo> getPromos() {
        Session currentSession = sessionFactory.getCurrentSession();

        List<Promo> promos = currentSession.createCriteria(Promo.class).list();
        logger.info("promos fetched from db: {}", promos);
        return promos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Promo promo) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.save(promo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAll(Iterable<Promo> promos) {
        Session currentSession = sessionFactory.getCurrentSession();
        for (Promo promo : promos) {
            currentSession.save(promo);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Promo promo) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.update(promo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Promo promo) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(promo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery(DELETE_ALL_HQL);
        return query.executeUpdate();
    }


}
