package com.my.ex.service;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.MessageDto;

public interface IMessageService {
	String findRoomId(Map<String, String> map);
	String generateRoomId(Map<String, String> map);
	void saveMessage(MessageDto dto);
	List<MessageDto> getPastMessages(String roomId);
	String getNicknameByUserId(String userId);
}
