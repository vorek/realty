package ru.realty.spring.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import ru.realty.spring.dao.UserDao;
import ru.realty.spring.domain.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@Override
	public User findByEmail(String email) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select distinct u from User u join fetch u.devices where u.email=:email");
		query.setParameter("email", email);
		List<User> result = query.list();
		return result.size() == 0? null : result.get(0);
	}
	
	@Override
	public User findByLogin(String login) {
		try {
		Query query = getSessionFactory().getCurrentSession().createQuery("from User where login=?")
				.setParameter(0, login);
		List<User> result = query.list();
		return result.size() == 0? null : result.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
