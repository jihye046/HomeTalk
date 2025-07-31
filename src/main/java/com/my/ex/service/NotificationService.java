package com.my.ex.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.ex.dao.NotificationDao;
import com.my.ex.dto.NotificationDto;

@Service
public class NotificationService implements INotificationService {

	@Autowired
	private NotificationDao dao;

	@Override
	public void addNotification(NotificationDto notificationDto) {
		dao.addNotification(notificationDto);
	}

	@Override
	public List<NotificationDto> getNotifications(String userId) {
		return dao.getNotifications(userId);
	}

	@Override
	public boolean updateReadStatus(int notificationId) {
		int result = dao.updateReadStatus(notificationId);
		return result > 0;
	}

	@Override
	public int getUnreadCount(String userId) {
		return dao.getUnreadCount(userId);
	}

	@Override
	public List<NotificationDto> getAllNotifications(String userId, int size, int offset) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("size", size);
		map.put("offset", offset);
		
		return dao.getAllNotifications(map);
	}
	
}
