package ru.realty.spring.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realty.spring.dto.ContactDto;
import ru.realty.spring.service.CommentService;

@RestController
public class CommentsController {
	
	@Autowired
	CommentService commentService;

	private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<List<ContactDto>> search(@RequestParam(value = "phone", required = true) String phone) {
		logger.info("Into searchController");
		return new ResponseEntity<>(commentService.searchComments(phone), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Object> add(@RequestBody JSONObject parameters) {
		logger.info("Adding comment");
		String deviceId = (String) parameters.get("deviceId");
		logger.info("Device ID="+deviceId);
		if (deviceId == null) {
			logger.info("device ID is null, will return UNAUTHORIZED");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		HttpStatus result = commentService.addComment((String)parameters.get("phone"), (String)parameters.get("comment"), deviceId);
		return new ResponseEntity<>(result);
	}
	
	@RequestMapping(value = "/sendtoall", method = RequestMethod.GET)
	public ResponseEntity<Object> sendMessageToAll(@RequestParam(value = "title", required = true) String title, 
			@RequestParam(value = "message", required = true) String message) {
		logger.info("Sending message to all");
		try {
			return new ResponseEntity<>(commentService.sendMessage(title, message, new LinkedList<String>()), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
	}
}
