package ru.realty.spring.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.realty.spring.controllers.CommentsController;
import ru.realty.spring.dao.ContactDao;
import ru.realty.spring.dao.DeviceDao;
import ru.realty.spring.domain.Comment;
import ru.realty.spring.domain.Contact;
import ru.realty.spring.domain.Device;
import ru.realty.spring.dto.ContactDto;
import ru.realty.spring.dto.CommentDto;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	ContactDao contactDao;
	
	@Autowired
	DeviceDao deviceDao;
	
	private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
	
	@Override
	public List<ContactDto> searchComments(String phone) {
		List<Contact> contactList = contactDao.findCommentsByPhoneNumber(phone);
		List<ContactDto> result = new LinkedList<ContactDto>();
		contactList.stream().forEach(con -> {
			ContactDto dto = new ContactDto();
			dto.setPhoneNumber(con.getPhone());
			List<CommentDto> commentsList = new LinkedList<CommentDto>();
			con.getComments().stream().forEach(comment -> {
				commentsList.add(new CommentDto(comment));
			});
			dto.setComment(commentsList);
			result.add(dto);
		});
		return result;
	}

	@Override
	public HttpStatus addComment(String phone, String comment, String deviceId) {
		logger.info("Into comment service/add comment");
		Contact contact = contactDao.findCommentsByPhoneNumber(phone).stream().findAny().orElse(null);
		logger.info("Contact ID="+(contact == null?null:contact.getId()));
		Device dev = deviceDao.findByDeviceId(deviceId);
		logger.info("Device ID="+(dev == null?null:dev.getId()));
		if (dev == null) {
			logger.info("data is null");
			return HttpStatus.UNAUTHORIZED;
		}
		if (contact == null) {
			contact = new Contact();
			contact.setPhone(phone);
			Comment com = new Comment();
			com.setComment(comment);
			com.setObject(contact);
			com.setDate(new Date());
			com.setUserId(dev.getUser().getId());
			contact.getComments().add(com);
		} else {
			Comment com = new Comment();
			com.setComment(comment);
			com.setObject(contact);
			contact.getComments().add(com);
		}
		contactDao.saveOrUpdate(contact);
		contactDao.flush();
		String message = "Новый номер появился в BlackList";
		String title = "База пополнена!";
		try {
			sendMessage(title, message, new LinkedList<String>());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HttpStatus.OK;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object sendMessage (String title, String mess, List<String> recepients) throws IOException {
		
       HttpURLConnection connection = (HttpURLConnection) new URL(GCM_SEND_ENDPOINT).openConnection();
       connection.setRequestMethod("POST");
       connection.setRequestProperty( "Content-Type", "application/json");
       connection.setRequestProperty( "Authorization", "key=AIzaSyCpj5CZF_MUVcKHY3ulsqlAOUgyhFoKsXw");
       
       String to = recepients.size() == 0? 
    		   "dZNta6KOXMo:APA91bEpWczS9YII-Mg6jAhVNuCjMGAghbrZVa0vf34_j6UWh27ogH7H3SMQZVKt9YQr5i8IxLeCuJOHGFqjfUI9k9s7f_VzPambLJIfQqB07xhXkYJSZgcPWwREs4o4lwOV1ya2X5CU"
    		   : recepients.get(0);
       
       JSONObject jsonBody = new JSONObject();
       try {
           jsonBody.put(PARAM_TO, to);
           jsonBody.put(PARAM_PRIORITY, "high");
           jsonBody.put(PARAM_VIBRATE, 1);
           Map<String, String> notificationParams = new HashMap<String, String>();
           notificationParams.put(PARAM_TITLE, title);
           notificationParams.put(PARAM_BODY, mess);
           notificationParams.put(PARAM_SOUND, "circles0.mp3");
           notificationParams.put(PARAM_COLOR, "#379F00");
           notificationParams.put(PARAM_ICON, "ic_stat_s");
           JSONObject jsonNotificationParams = new JSONObject(notificationParams);
           jsonBody.put(PARAM_JSON_NOTIFICATION_PARAMS, jsonNotificationParams);
       } catch (Exception e) {
    	   e.printStackTrace();
       }
       
       connection.setDoOutput(true);
       final OutputStream outputStream = connection.getOutputStream();
       try {
           outputStream.write(jsonBody.toString().getBytes());
       } finally{
           outputStream.close();
       }

       System.out.println(connection.getResponseCode() + ":" + connection.getResponseMessage());


       final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "Windows-1251"));
       try {
           for (String line  = reader.readLine(); line  != null; line  = reader.readLine()) {
               System.out.println(line);
           }
       } finally {
           reader.close();
       }
		/*
		 curl -s "https://gcm-http.googleapis.com/gcm/send" -H "Authorization: key=AIzaSyCpj5CZF_MUVcKHY3ulsqlAOUgyhFoKsXw" 
		 -H "Content-Type: application/json" -d 
		 '{"to": "dZNta6KOXMo:APA91bEpWczS9YII-Mg6jAhVNuCjMGAghbrZVa0vf34_j6UWh27ogH7H3SMQZVKt9YQr5i8IxLeCuJOHGFqjfUI9k9s7f_VzPambLJIfQqB07xhXkYJSZgcPWwREs4o4lwOV1ya2X5CU",
		 "priority" : "high", "vibrate": 1, 
		 "notification": { "title": "Hello!", "body" : "Oleg is the best!", "sound": "circles0.mp3", "color": "#379F00", "icon" : "ic_stat_s"}}'

		 */
		return null;
	}

	
	private static final String GCM_SEND_ENDPOINT = "https://gcm-http.googleapis.com/gcm/send";

    private static final String UTF8 = "UTF-8";

    private static final String PARAM_TO = "to";
    private static final String PARAM_PRIORITY = "priority";
    private static final String PARAM_VIBRATE = "vibrate";
    private static final String PARAM_COLLAPSE_KEY = "collapse_key";
    private static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
    private static final String PARAM_TIME_TO_LIVE = "time_to_live";
    private static final String PARAM_DRY_RUN = "dry_run";
    private static final String PARAM_RESTRICTED_PACKAGE_NAME = "restricted_package_name";

    private static final String PARAM_PLAINTEXT_PAYLOAD_PREFIX = "data.";

    private static final String PARAM_JSON_PAYLOAD = "data";
    private static final String PARAM_JSON_NOTIFICATION_PARAMS = "notification";

    public static final String RESPONSE_PLAINTEXT_MESSAGE_ID = "id";
    public static final String RESPONSE_PLAINTEXT_CANONICAL_REG_ID = "registration_id";
    public static final String RESPONSE_PLAINTEXT_ERROR = "Error";
    
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_SOUND = "sound";
    private static final String PARAM_COLOR = "color";
    private static final String PARAM_ICON = "icon";
}
