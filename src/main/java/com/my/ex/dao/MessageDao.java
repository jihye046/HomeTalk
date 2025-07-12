package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.ex.dto.MessageDto;

@Repository
public class MessageDao implements IMessageDao {

	private final String NAMESPACE = "com.my.ex.MessageMapper.";
	
	@Autowired
	private SqlSession session;
	
	@Override
	public String findRoomId(Map<String, String> map) {
		return session.selectOne(NAMESPACE + "findRoomId", map);
	}
	
	@Override
	public void saveMessage(MessageDto dto) {
		session.insert(NAMESPACE + "saveMessage", dto);
	}

	@Override
	public List<MessageDto> getPastMessages(String roomId) {
		return session.selectList(NAMESPACE + "getPastMessages", roomId);
	}

	@Override
	public String getNicknameByUserId(String userId) {
		return session.selectOne(NAMESPACE + "getNicknameByUserId", userId);
	}

}
