package com.cisco.sales.dao;

import com.cisco.sales.dto.Sale;
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
 * Date: 14.04.2014
 * Time: 23:50
 */
@Repository
public class HibernateSalesDao implements SalesDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Sale> getSales() {
        Session currentSession = sessionFactory.getCurrentSession();
	    List salesList = currentSession.createCriteria(Sale.class).list();

	    logger.info("{} sales fetched from DB", salesList.size());
	    return salesList;
    }

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void save(Sale sale) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.save(sale);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void update(Sale sale) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.saveOrUpdate(sale);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void update(List<Sale> sales) {
		Session currentSession = sessionFactory.getCurrentSession();
		for (Sale sale : sales) {
			currentSession.saveOrUpdate(sale);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void delete(Sale sale) {
		Session currentSession = sessionFactory.getCurrentSession();

		currentSession.delete(sale);
	}
}
