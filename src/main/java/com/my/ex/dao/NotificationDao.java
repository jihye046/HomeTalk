package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.ex.dto.NotificationDto;

@Repository
public class NotificationDao implements INotificationDao {

	private final String NAMESPACE = "com.my.ex.NotificationMapper.";

	@Autowired
	private SqlSession session;

	@Override
	public void addNotification(NotificationDto notificationDto) {
		session.insert(NAMESPACE + "addNotification", notificationDto);
	}

	@Override
	public List<NotificationDto> getNotifications(String userId) {
		return session.selectList(NAMESPACE + "getNotifications", userId);
	}

	@Override
	public int updateReadStatus(int notificationId) {
		return session.update(NAMESPACE + "updateReadStatus", notificationId);
	}

	@Override
	public int getUnreadCount(String userId) {
		return session.selectOne(NAMESPACE + "getUnreadCount", userId);
	}

	@Override
	public List<NotificationDto> getAllNotifications(Map<String, Object> map) {
		return session.selectList(NAMESPACE + "getAllNotifications", map);
	}

	@Override
	public int markAllAsRead(String userId) {
		return session.update(NAMESPACE + "markAllAsRead", userId);
	}

}
