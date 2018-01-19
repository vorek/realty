package ru.realty.spring.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.realty.spring.controllers.UserController;
import ru.realty.spring.dao.DeviceDao;
import ru.realty.spring.dao.UserDao;
import ru.realty.spring.domain.Device;
import ru.realty.spring.domain.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	DeviceDao deviceDao;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Override
	public HttpStatus addUser(Map<String, Object> parameters) {
		
		User user = userDao.findByEmail((String)parameters.get("email"));
		if (user != null) {
			return HttpStatus.NOT_ACCEPTABLE;
		} else {
			user = new User();
			user.setEmail((String)parameters.get("email"));
			user.setLogin((String) parameters.get("login"));
			user.setPassword((String) parameters.get("password"));
			user.setName((String) parameters.get("name"));
			
			Device device = new Device();
			device.setDeviceid((String) parameters.get("deviceId"));
			device.setUser(user);
			deviceDao.saveOrUpdate(device);
			
			List<Device> deviceList = new LinkedList<Device>();
			deviceList.add(device);
			user.setDevices(deviceList);
			userDao.saveOrUpdate(user);
			
			deviceDao.flush();
			userDao.flush();
			return HttpStatus.CREATED;
		}
	}

	@Override
	public HttpStatus addDevice(Map<String, Object> parameters) {
		logger.info("Getting parameters: login="+((String)parameters.get("login"))+", password: "+((String)parameters.get("password"))+", device ID="+((String)parameters.get("deviceId")));
		User user = userDao.findByLogin((String)parameters.get("login"));
		logger.info("Users found:"+(user!=null));
		if (user != null && user.getPassword().equals((String)parameters.get("password"))) {
			logger.info("Parameters is OK, lets add device");
			Device device = new Device();
			device.setDeviceid((String)parameters.get("deviceId"));
			device.setUser(user);
			deviceDao.saveOrUpdate(device);
			
			user.getDevices().add(device);
			userDao.saveOrUpdate(user);
			deviceDao.flush();
			userDao.flush();
			logger.info("Device added");
			return HttpStatus.OK;
		} else {
			logger.info("Something wrong");
			return HttpStatus.UNAUTHORIZED;
		}
	}

	
	
}
