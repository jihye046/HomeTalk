package com.my.ex.dto;

import lombok.Data;

@Data
public class MessageDto {
	private String code; // 상태코드
	
	private int messageId;
	private String roomId;
	private String sender; 
	private String receiver; 
	private String content; 
	private String regdate; 
}
