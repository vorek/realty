package ru.realty.spring.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface GenericDao<T> {
	
	T getById(Serializable id);

    T findById(Long id);

    void saveOrUpdate(T object);

    void merge(T object);

    void delete(T object);

    List<T> getALL();

    Session getSession();

    void butchInsert(List<T> list);

    List<T> findByParam(String name, Object value);

    List<T> findByParam(String name, Object value, String sortField, Boolean desc);

    List<T> findByParam(String name, Object value, String sortField);

    void flush();
}
