package com.cisco.accountmanager.dao;

import com.cisco.accountmanager.dto.AccountManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 21:34
 */
@Repository
public class HibernateAccountManagerDao implements AccountManagerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = REQUIRED)
    @Override
    public List<AccountManager> getAccountManagers() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createCriteria(AccountManager.class).list();
    }

    @Transactional(propagation = REQUIRED)
    @Override
    public void saveOrUpdate(List<AccountManager> managers) {
        Session currentSession = sessionFactory.getCurrentSession();
        for (AccountManager manager : managers) {
            currentSession.saveOrUpdate(manager);
        }
    }

    @Transactional(propagation = REQUIRED)
    @Override
    public void delete(AccountManager accountManager) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(accountManager);
    }
}
