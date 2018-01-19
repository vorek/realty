package ru.realty.spring.dto;

import java.util.List;

import ru.realty.spring.domain.Comment;

public class ContactDto {

	String phoneNumber;
	List<CommentDto> comment;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<CommentDto> getComment() {
		return comment;
	}
	public void setComment(List<CommentDto> list) {
		this.comment = list;
	}
	
}
