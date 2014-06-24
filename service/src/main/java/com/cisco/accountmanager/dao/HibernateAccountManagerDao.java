package com.cisco.accountmanager.dao;

import com.cisco.accountmanager.dto.AccountManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Rost
 * Date: 24.06.2014
 * Time: 21:34
 */
@Repository
public class HibernateAccountManagerDao implements AccountManagerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<AccountManager> getAccountManagers() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createCriteria(AccountManager.class).list();
    }
}
