package com.my.ex.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.my.ex.EnvironmentService;
import com.my.ex.dto.ChatRoomDto;
import com.my.ex.dto.MessageDto;
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
		List<String> roomIdList = service.getRoomId(userId); // hong1_hong3, hong1_hong2 2개
		
		List<ChatRoomDto> lastMessageList = new ArrayList<>();
		for(String roomId : roomIdList) {
			MessageDto messageDto = service.getLastMessage(roomId, searchText);
			if(messageDto != null) {
				messageDto.setRoomId(roomId);
				String receiver = service.getReceiver(roomId, userId, messageDto.getSender());
				int unreadMessageCount = service.getUnreadMessageCount(roomId, userId);
				String filename = userService.getProfileFilename(receiver);
				String imageUrl = "/user/getProfileImage/" + filename;
				
				// 목록에 보여줄 정보
				ChatRoomDto dto = new ChatRoomDto(messageDto, receiver, imageUrl, unreadMessageCount);
				lastMessageList.add(dto);
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
	@RequestMapping(value = "/setIsRead", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void setIsRead(@RequestBody Map<String, String> map) {
		service.setIsRead(map);
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
