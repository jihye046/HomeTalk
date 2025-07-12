package com.my.ex.dto;

import lombok.Data;

@Data
public class ChatRoomDto {
	
	private MessageDto messageDto;
	private String otherUserId;
	private String otherUserNickname;
	private String uprofile_image; // UserDto
	private int unreadMessageCount;
	
	public ChatRoomDto(MessageDto messageDto, String otherUserId, String otherUserNickname, String uprofile_image,
			int unreadMessageCount) {
		this.messageDto = messageDto;
		this.otherUserId = otherUserId;
		this.otherUserNickname = otherUserNickname;
		this.uprofile_image = uprofile_image;
		this.unreadMessageCount = unreadMessageCount;
	}
	
}
