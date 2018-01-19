package ru.realty.spring.dao;

import ru.realty.spring.domain.Device;

public interface DeviceDao extends GenericDao<Device> {

	Device findByDeviceId(String deviceId);
	
	
}
