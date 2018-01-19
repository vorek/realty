package ru.realty.spring.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import ru.realty.spring.dao.ContactDao;
import ru.realty.spring.domain.Contact;

@SuppressWarnings("deprecation")
@Repository
public class ContactDaoImpl extends GenericDaoImpl<Contact> implements ContactDao {

	public ContactDaoImpl() {
		super(Contact.class);
	}

	@Override
	public List<Contact> findCommentsByPhoneNumber(String phone) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select distinct c from Contact c join fetch c.comments where c.phone=:phone");
		query.setParameter("phone", phone);
		List<Contact> result = query.list();
		return result;
	}

}
