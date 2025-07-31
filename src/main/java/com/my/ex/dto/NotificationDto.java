package com.my.ex.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class NotificationDto {
	private int notificationId;
	private String userId; // 알림을 받을 사용자ID
	private String type; // 알림 종류(좋아요, 댓글, 팔로우 등)
	private int relatedId; // 알림과 관련된 주요 대상의 ID (예: 게시글 ID, 댓글 ID)
	private String senderId; // 알림을 발생시킨 사용자ID
	private String dataJson; // 댓글 미리 보기 내용 저장
	private String link;
	private String isRead;
	private Date createdAt;
}