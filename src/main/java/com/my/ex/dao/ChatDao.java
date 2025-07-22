package com.my.ex.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.ex.dto.MessageDto;

@Repository
public class ChatDao implements IChatDao {
	private final String NAMESPACE = "com.my.ex.MessageMapper.";
	
	@Autowired
	private SqlSession session;
	
	@Override
	public int getUnreadMessageTotalCount(String receiver) {
		return session.selectOne(NAMESPACE + "getUnreadMessageTotalCount", receiver);
	}
	
	@Override
	public List<MessageDto> getChatHistory(String roomId) {
		return session.selectList(NAMESPACE + "getPastMessages" , roomId);
	}

	@Override
	public List<String> getRoomId(String userId) {
		return session.selectList(NAMESPACE + "getRoomId", userId);
	}

	@Override
	public String getReceiver(Map<String, String> map) {
		return session.selectOne(NAMESPACE + "getReceiver", map);
	}

	@Override
	public int getUnreadMessageCount(Map<String, String> map) {
		return session.selectOne(NAMESPACE + "getUnreadMessageCount", map);
	}

	@Override
	public void setIsRead(Map<String, String> map) {
		session.update(NAMESPACE + "setIsRead", map);
	}

	@Override
	public List<MessageDto> getLastMessagesByUserId(Map<String, String> map) {
		return session.selectList(NAMESPACE + "getLastMessagesByUserId", map);
	}

}
