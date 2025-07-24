package com.my.ex.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.my.ex.EnvironmentService;
import com.my.ex.dto.ChatRoomDto;
import com.my.ex.dto.MessageDto;
import com.my.ex.server.ChatServer;
import com.my.ex.service.ChatService;
import com.my.ex.service.MessageService;
import com.my.ex.service.UserService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	// 안읽은 메시지 총 개수
	@RequestMapping("/getUnreadMessageTotalCount")
	public int getUnreadMessageTotalCount(@RequestParam String receiver) {
		return service.getUnreadMessageTotalCount(receiver);
	}
	
	// 채팅방 목록
	@RequestMapping("/getRoomList")
	public Map<String, Object> getRoomList(@RequestParam String userId,
										 @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText) {
		List<MessageDto> messageDtos = service.getLastMessagesByUserId(userId, searchText);
		List<ChatRoomDto> lastMessageList = new ArrayList<>(); // 마지막 메시지들만 담을 List<>
		
		if(messageDtos != null) {
			for(MessageDto messageDto  : messageDtos) {
				System.out.println(messageDto.getRoomId());
				String receiver = service.getReceiver(messageDto.getRoomId(), userId, messageDto.getSender());
				String receiverNickname = messageService.getNicknameByUserId(receiver);
				int unreadMessageCount = service.getUnreadMessageCount(messageDto.getRoomId(), userId);
				String filename = userService.getProfileFilename(receiver);
				String imageUrl = "/user/getProfileImage/" + filename;
				
				// 목록에 보여줄 정보
				ChatRoomDto chatRoomDto = new ChatRoomDto(messageDto, receiver, receiverNickname, imageUrl, unreadMessageCount);
				lastMessageList.add(chatRoomDto);
			}
		}
		
		// 웹소캣 연결 할 서버 주소
		String webSocketServer = environmentService.getWebSocketServer();
		
		Map<String, Object> map = new HashMap<>();
		map.put("rooms", lastMessageList);
		map.put("serverUrl", webSocketServer);
		return map;
	}
	
	// 특정 채팅방 내역
	@RequestMapping("/getChatHistory")
	public List<MessageDto> getChatHistory(@RequestParam String roomId) {
		List<MessageDto> chatHistory = new ArrayList<>();
		chatHistory = service.getChatHistory(roomId);
		
		return chatHistory; 
	}
	
	// 안읽음표시 없애기
	// 호출될 때 보낸 사람에게 WebSocket 알림을 보냄
	@RequestMapping(value = "/setIsRead", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void setIsRead(@RequestBody Map<String, String> map) {
		// 아직 안읽은 메시지의 sender 아이디 얻기
		Set<String> senderIds = service.getUniqueSenderIdsOfUnreadMessages(map);
		// 읽음 처리
		service.setIsRead(map);
		
		Gson gson = new Gson();
		for(String senderId : senderIds) {
			Map<String, Object> notification = new HashMap<>();
			notification.put("code", "5");
			notification.put("roomId", map.get("roomId"));
			
			String readNotificationMsg = gson.toJson(notification);
			
			ChatServer.sendMessageToUser(senderId, readNotificationMsg);
		}
	}
	
	// 신규 채팅인지 체크
	@RequestMapping("/hasRoomId")
	public String hasRoomId(@RequestParam String receiver, @RequestParam String sender) {
		Map<String, String> map = new HashMap<>();
		map.put("receiver", receiver);
		map.put("sender", sender);
		String roomId = null;
		roomId = messageService.findRoomId(map);
		return roomId;
	}
	
}
