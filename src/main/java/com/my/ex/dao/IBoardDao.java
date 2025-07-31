package com.my.ex.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.ex.dto.BoardDto;
import com.my.ex.dto.PostTagDto;
import com.my.ex.dto.TagDto;

public interface IBoardDao {
	int createBoard(BoardDto dto);
	
	
	List<BoardDto> findAll();
	BoardDto detailBoard(int bId);
	int updateBoard(BoardDto dto);
	int deleteBoard(int bId);
	void updateHitCount(int bId);
	
	// 태그
	void createTag(TagDto tag);
	int existsByTagName(String tagName);
	void addTagToPost(PostTagDto postTagDto);
	List<TagDto> findTagsByPostId(int bId);
	int findTagIdByName(String tagName);
	void deleteTagsByPostId(int bId);
	List<TagDto> getAllTags();
	
	// 게시글 좋아요
	int incrementLikesCount(int bId); 
	int decrementLikesCount(int bId);
	int getTotalLikes(int bId);
	
	// 북마크
	int incrementBookmarksCount(int bId); 
	int decrementBookmarksCount(int bId);
	int getTotalBookmarks(int bId);
	
	// 댓글 추천
	void incrementRecommendation(int bId);
	void decrementRecommendation(int bId);
	
	// 댓글 및 답글 등록, 삭제
	void replyInsert(BoardDto dto);
	void replyShape(int bGroup, int bStep);
	List<BoardDto> replyList(int bGroup);
	void replyChildInsert(BoardDto dto);
	List<BoardDto> replyChildList(int bGroup);
	int removeReply(Map<String, Object> map);
	int removeReplyIfNoChildReplies(Map<String, Integer> map);
	int updateCommentStep(Map<String, Integer> map);
	
	// targetComment가 몇 번째에 위치하는지 확인
	int getCommentOrderById(Map<String, Object> map);
	
	int boardCount();
	int incrementCommentCount(int bGroup);
	int decrementCommentCount(int bGroup);
	
	// 게시글 페이징
	List<BoardDto> pagingList(HashMap<String, Object> map);
	List<BoardDto> sort_hitPagingList(HashMap<String, Object> map);
	
	//댓글 페이징
	List<BoardDto> commentsPagingList(HashMap<String, Object> map);
	List<BoardDto> comments_sort_likePagingList(HashMap<String, Object> map);
	int commentsCount(int bGroup);
	
}
