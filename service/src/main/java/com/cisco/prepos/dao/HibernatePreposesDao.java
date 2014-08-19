package com.cisco.prepos.dao;

import com.cisco.prepos.dto.Prepos;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

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
    public List<Prepos> getPreposes(Prepos.Status... statuses) {
        Session currentSession = sessionFactory.getCurrentSession();
        Criteria criteria = createStatusesCriteria(currentSession, statuses);

        List<Prepos> preposesList = criteria.list();

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


    private Criteria createStatusesCriteria(Session currentSession, Prepos.Status[] statuses) {
        Criteria criteria = currentSession.createCriteria(Prepos.class);

        if (!isEmpty(statuses)) {
            Criterion[] statusesCriterions = new Criterion[statuses.length];
            for (int i = 0; i < statuses.length; i++) {
                statusesCriterions[i] = eq("status", statuses[i]);
            }
            criteria.add(or(statusesCriterions));
        }
        return criteria;
    }
}
