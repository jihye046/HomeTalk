package com.my.ex.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.my.ex.dto.MessageDto;
import com.my.ex.service.MessageService;

@Component
@ServerEndpoint(value="/chatServer", configurator = ServerEndpointConfigurator.class)
public class ChatServer {
	
	// 각각의 상태 관리를 위해 클라이언트들의 세션을 리스트에 담아 관리
	private static List<Session> sessionList = new ArrayList<>();
	
	@Autowired
	private MessageService service;

	// 연결이 성공적으로 이루어졌을 때 처리
	@OnOpen
	public void handleOpen(Session session) {
		sessionList.add(session);
		checkSessionList();
		clearSessionList();
	}
	
	// 클라이언트로부터 받은 메시지 처리
	@OnMessage
	public void handleMessage(String msg, Session session) {
		// JSON 문자열을 Java 객체로 변환
//		ObjectMapper mapper = new ObjectMapper();
//		Message message = mapper.readValue(msg, Message.class);
		Gson gson = new Gson();
		MessageDto message = gson.fromJson(msg, MessageDto.class);
		
		// 1: 새로운 유저일 때
		if(message.getCode().equals("1")) {
			Map<String, String> map = new HashMap<>();
			map.put("sender", message.getSender());
			map.put("receiver", message.getReceiver());
			String roomId = null;
			roomId = service.findRoomId(map);
			
			if(roomId != null) {
				sendPastMessagesToClient(session, roomId);
			} else {
				roomId = service.generateRoomId(map);
			}
			session.getUserProperties().put("roomId", roomId);
			message.setRoomId(roomId);
			
			
			for(Session s : sessionList) {
				if(s != session) { // 메시지를 보낸 클라이언트의 세션을 제외한 나머지 클라이언트(자기 자신에게 메시지를 보내지 않기 위해)
					sendMessageToSession(s, msg);
				}
			}
		}
		// 2: 기존 유저가 나감
		else if(message.getCode().equals("2")) {
			String senderUnickname = service.getNicknameByUserId(message.getSender());
			message.setSenderUnickname(senderUnickname);
			
			Gson updateGson = new Gson();
			String updateMsg = updateGson.toJson(message);
			
			sessionList.remove(session);
			for(Session s : sessionList) {
				sendMessageToSession(s, updateMsg);
			}
		}
		// 3: 메시지 전송
		else if(message.getCode().equals("3")) {
			String roomId = (String)session.getUserProperties().get("roomId");
			message.setRoomId(roomId);
			
			// 닉네임 조회 후 dto에 추가
			String senderUnickname = service.getNicknameByUserId(message.getSender());
			String receiverUnickname = service.getNicknameByUserId(message.getReceiver());
			message.setSenderUnickname(senderUnickname);
			message.setReceiverUnickname(receiverUnickname);
			
			// 메시지 저장
			service.saveMessage(message);
			
			// 클라이언트로 보낼 json 재생성
			Gson updateGson = new Gson();
			String updateMsg = updateGson.toJson(message);
			
			for(Session s : sessionList) {
				if(s != session) {
//					sendMessageToSession(s, msg);
					sendMessageToSession(s, updateMsg);
				}
			}
		} 
		// 4: 이모티콘 전송
		else if(message.getCode().equals("4")) {
			service.saveMessage(message);
			
			for(Session s : sessionList) {
				if(s != session) {
					sendMessageToSession(s, msg);
				}
			}
		}
	}
	
	// 접속자를 확인하는 메서드
	private void checkSessionList() {
		System.out.println("[Session List]");
		for (Session session : sessionList) {
			System.out.println(session.getId());
		}
		System.out.println();
	}
	
	// 연결이 끊어진 세션이 있으면 세션리스트에서 제거
	private void clearSessionList() {
		Iterator<Session> iterator = sessionList.iterator();
		while(iterator.hasNext()) {
			if(!(iterator.next()).isOpen()) {
				iterator.remove();
			}
		}
	}
	
	// 특정 세션에 메시지 보내기(json 문자열인 msg를 받아서 그대로 클라이언트에게 json 데이터를 보내고있음)
	private void sendMessageToSession(Session s, String msg) {
	    try {
	        s.getBasicRemote().sendText(msg); // getBasicRemote(): 세션과 관련된 소켓을 반환, sendText(): 클라이언트에게 메시지를 보냄
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// 과거 메시지를 클라이언트에게 전송
	private void sendPastMessagesToClient(Session session, String roomId) {
		List<MessageDto> pastMessages = service.getPastMessages(roomId);
		Gson gson = new Gson();
		for(MessageDto message: pastMessages) {
			message.setCode("1");
			sendMessageToSession(session, gson.toJson(message));
		}
	}
	
}
