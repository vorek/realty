package ru.realty.spring.service;

import java.util.Map;

import org.springframework.http.HttpStatus;

public interface UserService {
	
	HttpStatus addUser(Map<String, Object> parameters);

	HttpStatus addDevice(Map<String, Object> parameters);
	
}
