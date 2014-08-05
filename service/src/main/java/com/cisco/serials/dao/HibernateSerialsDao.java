package com.cisco.serials.dao;

import com.cisco.serials.dto.Serial;
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
public class HibernateSerialsDao implements SerialsDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveOrUpdate(List<Serial> serials) {
        Session currentSession = sessionFactory.getCurrentSession();

        for (Serial serial : serials) {
            currentSession.saveOrUpdate(serial);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Serial> getAllSerials() {
        Session currentSession = sessionFactory.getCurrentSession();
        List<Serial> serials = currentSession.createCriteria(Serial.class).list();
        logger.info("fetched serials from db: {}", serials);
        return serials;
    }
}
