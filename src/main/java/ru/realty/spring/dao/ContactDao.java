package ru.realty.spring.dao;

import java.util.List;

import ru.realty.spring.domain.Comment;
import ru.realty.spring.domain.Contact;

public interface ContactDao extends GenericDao<Contact> {

	List<Contact> findCommentsByPhoneNumber(String phone);

}
