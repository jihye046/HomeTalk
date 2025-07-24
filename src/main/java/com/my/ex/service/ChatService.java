package com.my.ex.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.ex.dao.ChatDao;
import com.my.ex.dto.MessageDto;

@Service
public class ChatService implements IChatService {

	@Autowired
	private ChatDao dao;
	
	@Override
	public int getUnreadMessageTotalCount(String receiver) {
		return dao.getUnreadMessageTotalCount(receiver);
	}
	
	@Override
	public List<MessageDto> getChatHistory(String roomId) {
		return dao.getChatHistory(roomId);
	}
	
	@Override
	public List<String> getRoomId(String userId) {
		return dao.getRoomId(userId);
	}

	@Override
	public String getReceiver(String roomId, String userId, String sender) {
		Map<String, String> map = new HashMap<>();
		map.put("roomId", roomId);
		map.put("userId", userId);
		map.put("sender", sender);
		
		return dao.getReceiver(map);
	}

	@Override
	public int getUnreadMessageCount(String roomId, String userId) {
		Map<String, String> map = new HashMap<>();
		map.put("roomId", roomId);
		map.put("userId", userId);
		
		return dao.getUnreadMessageCount(map);
	}

	@Override
	public void setIsRead(Map<String, String> map) {
		dao.setIsRead(map);
	}

	@Override
	public List<MessageDto> getLastMessagesByUserId(String userId, String searchText) {
		Map<String, String>map = new HashMap<>();
		map.put("userId", userId);
		map.put("searchText", searchText);
		
		return dao.getLastMessagesByUserId(map);
	}

	@Override
	public Set<String> getUniqueSenderIdsOfUnreadMessages(Map<String, String> map) {
		return dao.getUniqueSenderIdsOfUnreadMessages(map);
	}

}
