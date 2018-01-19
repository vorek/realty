package ru.realty.spring.dto;

import ru.realty.spring.domain.Comment;

public class CommentDto {

	String date;
	String comment;
	
	public CommentDto(Comment comment) {
		this.date = comment.getDate().toString();
		this.comment = comment.getComment();
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
