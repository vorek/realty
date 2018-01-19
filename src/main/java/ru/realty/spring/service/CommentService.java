package ru.realty.spring.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import ru.realty.spring.dto.ContactDto;

public interface CommentService {

	public List<ContactDto> searchComments(String phone);

	public Object sendMessage(String title, String message, List<String> recepients) throws IOException;

	public HttpStatus addComment(String phone, String comment, String deviceId);

}
