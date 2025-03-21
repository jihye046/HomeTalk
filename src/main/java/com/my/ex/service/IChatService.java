package com.my.ex.service;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.MessageDto;

public interface IChatService {
	int getUnreadMessageTotalCount(String receiver);
	List<MessageDto> getChatHistory(String roomId);
	List<String> getRoomId(String userId);
	MessageDto getLastMessage(String roomId, String searchText);
	String getReceiver(String roomId, String userId, String sender);
	int getUnreadMessageCount(String roomId, String userId);
	void setIsRead(Map<String, String> map);
}
