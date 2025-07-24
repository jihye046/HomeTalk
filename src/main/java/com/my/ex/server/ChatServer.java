package com.my.ex.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
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
	
	// 모든 클라이언트의 웹소켓 세션을 담는 리스트
	// 누가 어느 방에 있든 접속한 모든 사람의 세션이 여기에 들어감
	private static List<Session> sessionList = new ArrayList<>();
	
	// 방(roomId) 별로 접속한 사용자 세션을 관리하는 맵
	// key = roomId, value = List<Session>
	// List<Session>: 해당 방에 접속해 있는 유저들의 세션들
	private static Map<String, List<Session>> roomSessionMap = new ConcurrentHashMap<>(); // Map<"roomId", List<"roomUserId", "userA">"
	
	// 사용자 ID별로 웹소켓 세션을 관리하는 맵(특정 사용자에게 메시지 전송용)
	// key = userId, value = Session
	private static Map<String, Session> userSessionMap = new ConcurrentHashMap<>(); 
	
	@Autowired
	private MessageService service;

	// 연결이 성공적으로 이루어졌을 때 처리
	@OnOpen
	public void handleOpen(Session session) {
		sessionList.add(session);
		String userId = (String) session.getUserProperties().get("userId");
		if(userId != null) {
			userSessionMap.put(userId, session);
		}
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
			
			// rooId 찾거나 생성
			String roomId = null;
			roomId = service.findRoomId(map);
			if(roomId != null) {
				sendPastMessagesToClient(session, roomId);
			} else {
				roomId = service.generateRoomId(map);
			}
			message.setRoomId(roomId);
			
			// 세션에 저장
			session.getUserProperties().put("roomId", roomId);
			session.getUserProperties().put("roomUserId", message.getSender()); // 현재 대화방에 접속한 유저의 userId
			
			// roomId 이름의 ArrayList가 없으면 만들고 session을 저장
			roomSessionMap.computeIfAbsent(roomId, key -> new ArrayList<>()).add(session);
			
			// 메시지 전송
			/*
			for(Session s : sessionList) {
				if(s != session) { // 메시지를 보낸 클라이언트의 세션을 제외한 나머지 클라이언트(자기 자신에게 메시지를 보내지 않기 위해)
					sendMessageToSession(s, msg);
				}
			}
			*/
		}
		// 2: 기존 유저가 나감
		else if(message.getCode().equals("2")) {
			String senderUnickname = service.getNicknameByUserId(message.getSender());
			message.setSenderUnickname(senderUnickname);
			
			String roomId = (String) session.getUserProperties().get("roomId");
		    String roomUserId = (String) session.getUserProperties().get("roomUserId");
			
			if(roomId != null && roomUserId != null) {
				List<Session> sessions = roomSessionMap.get(roomId);
				if(sessions != null) {
					sessions.remove(session); // 해당 세션을 방에서 제거
					if(sessions.isEmpty()) {
						roomSessionMap.remove(roomId); // 방에 남은 사용자의 세션이 없으면 방 자체도 제거
					}
				}
			}
		    
			Gson updateGson = new Gson();
			String updateMsg = updateGson.toJson(message);
			
			// 나간 사용자의 세션을 모든 세션에서 제거 (sessionList)
			sessionList.remove(session);
			userSessionMap.remove(roomUserId);
			
			// 현재 남아있는 사용자들이 있다면 메시지 전송 
			if(roomId != null && roomSessionMap.containsKey(roomId)) {
				for(Session s : sessionList) {
					sendMessageToSession(s, updateMsg);
				}
			}
		}
		// 3: 일반 메시지 전송
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
			String updateMsg = updateGson.toJson(message); // messageId가 포함된 JSON
			
			List<Session> roomSessions = roomSessionMap.get(roomId); // 해당 방에 접속한 유저들
			if(roomSessions != null) {
				// 해당 방(roomId)에 접속한 유저들에게만 메시지 전송
				for(Session s : roomSessions) {
					sendMessageToSession(s, updateMsg);
				}
			}
		} 
		// 4: 이모티콘 전송
		else if(message.getCode().equals("4")) {
			String roomId = (String) session.getUserProperties().get("roomId");
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
			
			List<Session> roomSessions = roomSessionMap.get(roomId); // 해당 방에 접속한 유저들
			if(roomSessions != null) {
				for(Session s : roomSessions) {
					sendMessageToSession(s, updateMsg);
				}
			}
		}
	}
	
	@OnClose
	public void handleClose(Session session) {
		System.out.println("웹소켓 세션 닫힘: " + session.getId());
		sessionList.remove(session);
		String userId = (String) session.getUserProperties().get("userId");
		if(userId != null) {
			userSessionMap.remove(userId);
		}
		
		String roomId = (String) session.getUserProperties().get("roomId");
		if(roomId != null) {
			List<Session> sessionsInRoom = roomSessionMap.get(roomId);
			if(sessionsInRoom != null) {
				sessionsInRoom.remove(session);
				if(sessionsInRoom.isEmpty()) {
					roomSessionMap.remove(roomId);
				}
			}
		}
		checkSessionList();
	}
	
	@OnError
	public void handleError(Throwable throwable, Session session) {
		System.out.println("웹소켓 에러 발생: " + session.getId());
		throwable.printStackTrace();
		sessionList.remove(session);
		checkSessionList();
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
	
	// 특정 사용자에게 메시지 전송
	public static void sendMessageToUser(String userId, String message) {
		Session userSession = userSessionMap.get(userId);
		if(userSession != null && userSession.isOpen()) {
			try {
				userSession.getBasicRemote().sendText(message);
//				System.out.println("웹소켓 메시지 전송 (대상: " + userId + "): " + message);
			} catch (IOException e) {
//				System.err.println("웹소켓 메시지 전송 실패 (대상: " + userId + "): " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("대상 사용자 (" + userId + ")의 웹소켓 세션이 없거나 닫혀있음.");
		}
	}
	
}
