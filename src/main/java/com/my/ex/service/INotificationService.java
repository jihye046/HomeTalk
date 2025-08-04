package com.my.ex.service;

import java.util.List;

import com.my.ex.dto.NotificationDto;

public interface INotificationService {
	void addNotification(NotificationDto notificationDto);
	List<NotificationDto> getNotifications(String userId);
	List<NotificationDto> getAllNotifications(String userId, int size, int offset);
	boolean updateReadStatus(int notificationId);
	int getUnreadCount(String userId);
	boolean markAllAsRead(String userId);
}
