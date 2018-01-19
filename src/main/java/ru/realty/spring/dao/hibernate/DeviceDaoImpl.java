package ru.realty.spring.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.realty.spring.dao.DeviceDao;
import ru.realty.spring.domain.Contact;
import ru.realty.spring.domain.Device;
import ru.realty.spring.service.CommentService;

@Repository
public class DeviceDaoImpl extends GenericDaoImpl<Device> implements DeviceDao {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceDao.class);
	
	public DeviceDaoImpl() {
		super(Device.class);
	}

	@Override
	public Device findByDeviceId(String deviceId) {
		try {
			logger.info("Into DeviceDaoImpl/FindByDeviceId. Device ID=" + deviceId);
			Query query = getSessionFactory().getCurrentSession().createQuery("from Device where deviceid=?")
					.setParameter(0, deviceId);
			logger.info("Query: " + query.toString());
			List<Device> result = query.list();
			logger.info("Results size: " + result.size());
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
}
