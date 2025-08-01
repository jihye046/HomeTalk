package com.my.ex.dto;

import lombok.Data;

@Data
public class ChatRoomDto {
	
	private MessageDto messageDto;
	private String otherUserId;
	private String otherUserNickname;
	private String uprofileImage; // UserDto
	private int unreadMessageCount;
	
	public ChatRoomDto(MessageDto messageDto, String otherUserId, String otherUserNickname, String uprofileImage,
			int unreadMessageCount) {
		this.messageDto = messageDto;
		this.otherUserId = otherUserId;
		this.otherUserNickname = otherUserNickname;
		this.uprofileImage = uprofileImage;
		this.unreadMessageCount = unreadMessageCount;
	}
	
}
