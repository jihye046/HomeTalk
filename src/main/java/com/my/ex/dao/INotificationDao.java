package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.NotificationDto;

public interface INotificationDao {
	void addNotification(NotificationDto notificationDto);
	List<NotificationDto> getNotifications(String userId);
	List<NotificationDto> getAllNotifications(Map<String, Object> map);
	int updateReadStatus(int notificationId);
	int getUnreadCount(String userId);
	int markAllAsRead(String userId);
}
