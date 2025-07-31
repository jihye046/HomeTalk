package com.my.ex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.ex.dao.BoardDao;
import com.my.ex.dao.LikeDao;
import com.my.ex.dto.LikeDto;
import com.my.ex.dto.NotificationDto;

@Service
public class LikeService implements ILikeService{
	
	@Autowired
	private LikeDao dao;
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private NotificationService notificationService;
	
	// 게시글 작성자가 아닌 사용자가 좋아요를 눌렀을 때만 알림 생성
	@Override
	public void addLike(LikeDto dto, NotificationDto notificationDto) {
		dao.addLike(dto);
		
		if(!notificationDto.getUserId().equals(notificationDto.getSenderId())) {
			notificationService.addNotification(notificationDto);
		}
	}

	@Override
	public void removeLike(LikeDto dto) {
		dao.removeLike(dto);
	}

	@Override
	public boolean isLiked(int bId, String userId) {
		int result = dao.isLiked(bId, userId);
		return result > 0;
	}

	@Override
	public void addRecommend(LikeDto dto) {
		dao.addRecommend(dto);
	}

	@Override
	public void removeRecommend(LikeDto dto) {
		dao.removeRecommend(dto);
	}

	@Override
	public boolean isRecommended(int bId, String userId) {
		return dao.isRecommended(bId, userId) > 0;
	}
	
}
