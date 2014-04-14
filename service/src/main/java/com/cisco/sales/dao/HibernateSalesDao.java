package com.cisco.sales.dao;

import com.cisco.sales.dto.Sale;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Rost
 * Date: 14.04.2014
 * Time: 23:50
 */
@Repository
public class HibernateSalesDao implements SalesDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public List<Sale> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createCriteria(Sale.class).list();
    }
}
