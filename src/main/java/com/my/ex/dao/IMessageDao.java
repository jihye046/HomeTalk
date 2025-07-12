package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.MessageDto;

public interface IMessageDao {
	String findRoomId(Map<String, String> map);
	void saveMessage(MessageDto dto);
	List<MessageDto> getPastMessages(String roomId);
	String getNicknameByUserId(String userId);
}
