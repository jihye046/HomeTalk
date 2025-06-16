package com.my.ex.service;

import java.util.List;
import java.util.Map;

import com.my.ex.dto.BoardDto;
import com.my.ex.dto.BoardPagingDto;
import com.my.ex.dto.CommentsPagingDto;
import com.my.ex.dto.TagDto;

public interface IBoardService {
	boolean createBoard(BoardDto dto);
	
	
	List<BoardDto> findAll();
	BoardDto detailBoard(int bId);
	boolean updateBoard(BoardDto dto);
	boolean deleteBoard(int bId);
	void updateHitCount(int bId);
	int incrementLikesCount(int bId);
	int decrementLikesCount(int bId);
	int getTotalLikes(int bId);
	int commentsCount(int bGroup);
	
	// 태그
	void createTag(int bId, List<TagDto> tags);
	boolean existsByTagName(String tagName);
	List<TagDto> findTagsByPostId(int bId);
	
	// 게시글 좋아요
	int incrementBookmarkAndGetCount(int bId);
	int decrementBookmarkAndGetCount(int bId);
	
	// 댓글 추천
	int incrementRecommendationAndGetCount(int bId);
	int decrementRecommendationAndGetCount(int bId);
	
	// 댓글 및 답글 등록, 삭제
	void replyInsert(BoardDto dto);
	void replyChildInsert(BoardDto dto);
	int incrementCommentCount(int bGroup);
	int decrementCommentCount(int bGroup);
	boolean removeReply(Map<String, Object> map);
	boolean updateCommentStep(Map<String, Integer> map);
	boolean removeReplyIfNoChildReplies(Map<String, Integer> map);
	
	int getTotalBookmarks(int bId);
	List<BoardDto> replyList(int bGroup);
	List<BoardDto> replyChildList(int bGruop);

	// 게시글 페이징
	List<BoardDto> pagingList(int page, String searchGubun, String searchText, String sortType);
	List<BoardDto> sort_hitPagingList(int page, String searchGubun, String searchText);
	BoardPagingDto pagingParam(int page); // 페이징처리 시 하단에 보일 번호
	
	// 댓글 페이징
	List<BoardDto> commentsPagingList(int page, String sortType, int bGroup);
	CommentsPagingDto commentsPagingParam(int page, int bGroup); // 페이징처리 시 하단에 보일 번호
	
}
