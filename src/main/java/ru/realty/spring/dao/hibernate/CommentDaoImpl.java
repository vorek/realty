package ru.realty.spring.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import ru.realty.spring.dao.CommentDao;
import ru.realty.spring.domain.Comment;

@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment> implements CommentDao {

	public CommentDaoImpl() {
		super(Comment.class);
	}

}
