package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.MessageDto;

public interface IChatDao {
	int getUnreadMessageTotalCount(String receiver);
	List<MessageDto> getChatHistory(String roomId);
	List<String> getRoomId(String userId);
	String getReceiver(Map<String, String> map);
	int getUnreadMessageCount(Map<String, String> map);
	void setIsRead(Map<String, String> map);
	List<MessageDto> getLastMessagesByUserId(Map<String, String> map);
}
