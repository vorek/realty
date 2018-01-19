package ru.realty.spring.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.realty.spring.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public ResponseEntity<Object> addUser(@RequestBody Map<String, Object> parameters) {
		logger.info("Adding user");
		return new ResponseEntity<>(userService.addUser(parameters));
	}
	
	@RequestMapping(value = "/adddevice", method = RequestMethod.POST)
	public ResponseEntity<Object> addDevice(@RequestBody Map<String, Object> parameters) {
		logger.info("Adding device");
		return new ResponseEntity<>(userService.addDevice(parameters));
	}
}
