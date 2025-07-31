package com.my.ex.service;

import com.my.ex.dto.LikeDto;
import com.my.ex.dto.NotificationDto;

public interface ILikeService {
	void addLike(LikeDto dto, NotificationDto notificationDto);
	void removeLike(LikeDto dto);
	boolean isLiked(int bId, String userId);
	void addRecommend(LikeDto dto);
	void removeRecommend(LikeDto dto);
	boolean isRecommended(int bId, String userId);
}
