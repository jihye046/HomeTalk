package com.my.ex.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.ex.dao.MessageDao;
import com.my.ex.dto.MessageDto;

@Service
public class MessageService implements IMessageService {
	
	@Autowired
	private MessageDao dao;
	
	@Override
	public String findRoomId(Map<String, String> map) {
		return dao.findRoomId(map);
	}
	
	@Override
	public String generateRoomId(Map<String, String> map) {
		String sender = map.get("sender");
		String receiver = map.get("receiver");
		
		String roomId;
		roomId = sender.compareTo(receiver) < 0 
				 ? sender + "_" + receiver 
				 : receiver + "_" + sender;
		
		return roomId;
	}

	@Override
	public void saveMessage(MessageDto dto) {
		dao.saveMessage(dto);
	}

	@Override
	public List<MessageDto> getPastMessages(String roomId) {
		return dao.getPastMessages(roomId);
	}

	@Override
	public String getNicknameByUserId(String userId) {
		return dao.getNicknameByUserId(userId);
	}

}
