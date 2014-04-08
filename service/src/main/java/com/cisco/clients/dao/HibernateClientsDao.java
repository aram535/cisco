package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Rost
 * Date: 08.04.2014
 * Time: 22:00
 */
@Repository
public class HibernateClientsDao implements ClientsDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Client> getClients() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createCriteria(Client.class).list();
    }
}