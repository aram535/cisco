package com.cisco.clients.dao;

import com.cisco.clients.dto.Client;
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
 * User: Rost
 * Date: 08.04.2014
 * Time: 22:00
 */
@Repository
public class HibernateClientsDao implements ClientsDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Client> getClients() {
        Session currentSession = sessionFactory.getCurrentSession();

        List<Client> clients = currentSession.createCriteria(Client.class).list();
        logger.info("Client fetched from db: {}", clients);
        return clients;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Client client) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.save(client);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Client client) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(client);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Client client) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(client);
    }

}
