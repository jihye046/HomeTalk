package com.my.ex.dto;

import java.util.List;

import lombok.Data;

@Data
public class NotificationResponseDto {
	private List<NotificationDto> notificationDtos;
	private int unreadCount;
	private boolean readStatusUpdated;
	private boolean allReadStatusesUpdated ;
}
