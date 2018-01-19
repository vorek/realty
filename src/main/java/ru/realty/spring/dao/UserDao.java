package ru.realty.spring.dao;

import ru.realty.spring.domain.User;

public interface UserDao extends GenericDao<User>{

	User findByEmail(String string);

	User findByLogin(String string);

}
