package com.my.ex.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.ex.dto.NotificationDto;
import com.my.ex.dto.NotificationResponseDto;
import com.my.ex.service.NotificationService;

@Controller
@RequestMapping("/notification")
public class NotificationController {
	
	@Autowired
	private NotificationService service;
	
	// 알림 아이콘 눌렀을 때 조회(최근 3일 내역만)
	@GetMapping("/getNotifications")
	@ResponseBody
	public NotificationResponseDto getNotification(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		
		List<NotificationDto> notificationList = service.getNotifications(userId);
		int unreadCount = service.getUnreadCount(userId);
		
		NotificationResponseDto responseDto = new NotificationResponseDto();
		responseDto.setNotificationDtos(notificationList);
		responseDto.setUnreadCount(unreadCount);
		
		return responseDto;
	}
	
	// 모든 알림 보기 눌렀을 때(모든 알림 조회)
	@GetMapping("/getAllNotifications")
	public String getAllNotifications(
			HttpSession session, 
			Model model,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
		
		String userId = (String) session.getAttribute("userId");
		int offset = (page - 1) * size; // 몇 번째부터 가져올지(인덱스 기준)
		
		List<NotificationDto> notificationAllList = service.getAllNotifications(userId, size, offset);
		
		// 응답 데이터를 js에서 사용할 수 있도록 List -> JSON 변환
		// JSON 변환 없이는 js에서 forEach() 사용 안됨
		ObjectMapper mapper = new ObjectMapper();
		String jsonList = mapper.writeValueAsString(notificationAllList);
		
		model.addAttribute("notificationAllListJson", jsonList);
		
		return "/user/getNotifications";
	}
	
	// 모든 알림 보기 눌렀을 때(모든 알림 조회) - 옵저버
	@GetMapping("/getAllNotifications/axios")
	@ResponseBody
	public List<NotificationDto> getAllNotificationsAxios(
			HttpSession session, 
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
		
		String userId = (String) session.getAttribute("userId");
		int offset = (page - 1) * size; // 몇 번째부터 가져올지(인덱스 기준)
		
		return service.getAllNotifications(userId, size, offset);
	}
	
	// 읽음 상태 업데이트
	@PatchMapping("/updateReadStatus")
	@ResponseBody
	public NotificationResponseDto updateReadStatus(@RequestBody NotificationDto dto, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		
		boolean isUpdated = service.updateReadStatus(dto.getNotificationId());
		int unreadCount = service.getUnreadCount(userId);
		
		NotificationResponseDto responseDto = new NotificationResponseDto();
		responseDto.setReadStatusUpdated(isUpdated);
		responseDto.setUnreadCount(unreadCount);
		
		return responseDto;
	}
	
	// 모두 읽기 처리
	@PatchMapping("/markAllAsRead")
	@ResponseBody
	public NotificationResponseDto markAllAsRead(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		boolean isUpdated = service.markAllAsRead(userId);
		int unreadCount = service.getUnreadCount(userId);
		
		NotificationResponseDto responseDto = new NotificationResponseDto();
		responseDto.setAllReadStatusesUpdated(isUpdated);
		responseDto.setUnreadCount(unreadCount);
		
		return responseDto;
	}
	
}
