package ru.realty.spring.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Comments")
public class Comment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="comment")
	String comment;
	
	@Column(name="date")
	Date date;
	
	@Column(name="user_id")
	Long userId;
	
	@ManyToOne
    @JoinColumn(name = "contact_id")
    Contact object;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public Contact getObject() {
		return object;
	}


	public void setObject(Contact object) {
		this.object = object;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
}
