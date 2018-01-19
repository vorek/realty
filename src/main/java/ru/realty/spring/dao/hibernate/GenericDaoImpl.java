package ru.realty.spring.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import ru.realty.spring.dao.GenericDao;

@Repository
public abstract class GenericDaoImpl<T> extends HibernateDaoSupport implements GenericDao<T> {
	
	protected Class<T> clazz;

    public static final int BATCH_SIZE = 45;


    @Autowired
    public void setSessionFactoryAutowired(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }


    public GenericDaoImpl(Class<T> type) {
        clazz = type;
    }

    public T getById(Serializable id) {
        return (T) currentSession().get(clazz, id);
    }


    public T findById(Long id) {
        Query query = currentSession().createQuery("from " + clazz.getSimpleName() + " where id=" + id);
        if (!query.list().isEmpty()) {
            return (T) query.list().get(0);
        }
        throw new IllegalStateException("There is no object with ID=" + id);
    }


    public List<T> findByParam(String name, Object value) {
        List<T> result = currentSession()
                .createCriteria(clazz).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .add(Restrictions.eq(name, value)).list();
        return result;
    }

    public List<T> findByParam(String name, Object value, String sortColumn, Boolean desc) {
        Order order = null;
        if (sortColumn != null) {
            if (desc != null && desc) {
                order = Order.desc(sortColumn);
            } else {
                order = Order.asc(sortColumn);
            }
        }
        Criteria crit = currentSession()
                .createCriteria(clazz)
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .add(Restrictions.eq(name, value));
        if (order != null) {
            crit.addOrder(order);
        }
        List<T> result = crit.list();
        return result;
    }

    public List<T> findByParam(String name, Object value, String sortColumn) {
        return findByParam(name, value, sortColumn, false);
    }


    public void saveOrUpdate(T object) {
        currentSession().saveOrUpdate(object);
    }


    @Override
    public void merge(T object) {
        currentSession().merge(object);
    }

    public void delete(T object) {
        currentSession().delete(object);
    }


    public List<T> getALL() {

        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(clazz)
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Override
    public Session getSession() {
        return currentSession();
    }

    public void butchInsert(List<T> list) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            session.save(list.get(i));
            if (i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();
            }
        }
        tx.commit();
        session.close();
    }

    @Override
    public void flush() {
        currentSession().flush();
    }
	
}
