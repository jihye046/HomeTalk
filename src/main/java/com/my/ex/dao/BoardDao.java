package com.my.ex.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.ex.dto.BoardDto;
import com.my.ex.dto.PostTagDto;
import com.my.ex.dto.TagDto;

@Repository
public class BoardDao implements IBoardDao {
	
	private final String NAMESPACE = "com.my.ex.BoardMapper.";

	@Autowired
	private SqlSession session;

	@Override
	public int createBoard(BoardDto dto) {
		return session.insert(NAMESPACE + "createBoard", dto); 
	}

	@Override
	public List<BoardDto> findAll() {
		return session.selectList(NAMESPACE + "findAll");
	}

	@Override
	public BoardDto detailBoard(int bId) {
		return session.selectOne(NAMESPACE + "detailBoard", bId);
	}

	@Override
	public int updateBoard(BoardDto dto) {
		return session.update(NAMESPACE + "updateBoard", dto);
	}

	@Override
	public int deleteBoard(int bId) {
		return session.delete(NAMESPACE + "deleteBoard", bId);
	}

	@Override
	public void updateHitCount(int bId) {
		session.update(NAMESPACE + "updateHitCount", bId);
	}
	
	@Override
	public int incrementLikesCount(int bId) {
		return session.update(NAMESPACE + "incrementLikesCount", bId);
	}
	
	@Override
	public int decrementLikesCount(int bId) {
		return session.update(NAMESPACE + "decrementLikesCount", bId);
	}
	
	@Override
	public int getTotalLikes(int bId) {
		return session.selectOne(NAMESPACE + "getTotalLikes", bId);
	}
	
	@Override
	public int incrementBookmarksCount(int bId) {
		return session.update(NAMESPACE + "incrementBookmarksCount", bId);
	}

	@Override
	public int decrementBookmarksCount(int bId) {
		return session.update(NAMESPACE + "decrementBookmarksCount", bId);
	}
	
	@Override
	public void incrementRecommendation(int bId) {
		session.update(NAMESPACE + "incrementLikesCount", bId);
	}

	@Override
	public void decrementRecommendation(int bId) {
		session.update(NAMESPACE + "decrementLikesCount", bId);
	}
	
	@Override
	public int getTotalBookmarks(int bId) {
		return session.selectOne(NAMESPACE + "getTotalBookmarks", bId);
	}

	@Override
	public void replyInsert(BoardDto dto) {
		replyShape(dto.getbGroup(), dto.getbStep());
		session.insert(NAMESPACE + "replyInsert", dto);
	}

	@Override
	public void replyShape(int bGroup, int bStep) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("bGroup", bGroup);
		map.put("bStep", bStep);
		session.update(NAMESPACE + "replyShape", map);
	}

	@Override
	public List<BoardDto> replyList(int bGroup) {
		return session.selectList(NAMESPACE + "replyList", bGroup);
	}

	@Override
	public void replyChildInsert(BoardDto dto) {
		replyShape(dto.getbGroup(), dto.getbStep());
		session.insert(NAMESPACE + "replyChildInsert", dto);
	}

	@Override
	public List<BoardDto> replyChildList(int bGroup) {
		return session.selectList(NAMESPACE + "replyChildList", bGroup);
	}

	@Override
	public List<BoardDto> pagingList(HashMap<String, Object> map) {
		return session.selectList(NAMESPACE + "pagingList", map);
	}
	
	@Override
	public List<BoardDto> sort_hitPagingList(HashMap<String, Object> map) {
		return session.selectList(NAMESPACE + "sort_hitPagingList", map);
	}
	
	@Override
	public List<BoardDto> commentsPagingList(HashMap<String, Object> map) {
		return session.selectList(NAMESPACE + "commentsPagingList", map);
	}

	@Override
	public List<BoardDto> comments_sort_likePagingList(HashMap<String, Object> map) {
		return session.selectList(NAMESPACE + "commentsPagingList", map);
	}

	@Override
	public int boardCount() {
		return session.selectOne(NAMESPACE + "boardCount");
	}

	@Override
	public int incrementCommentCount(int bGroup) {
		session.update(NAMESPACE + "incrementCommentCount", bGroup);
		return session.selectOne(NAMESPACE + "commentsCount", bGroup);
	}
	
	@Override
	public int decrementCommentCount(int bGroup) {
		session.update(NAMESPACE + "decrementCommentCount", bGroup);
		return session.selectOne(NAMESPACE + "commentsCount", bGroup);
	}

	@Override
	public int commentsCount(int bGroup) {
//		return session.selectOne(NAMESPACE + "commentsCount", bGroup);
		return session.selectOne(NAMESPACE + "commentsAllCount", bGroup);
	}

	@Override
	public int removeReply(Map<String, Object> map) {
 		int updateCount = session.update(NAMESPACE + "updateCommentContent", map);
		int deleteCount = session.delete(NAMESPACE + "removeComment", map);

		if(updateCount > 0 || deleteCount > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public int removeReplyIfNoChildReplies(Map<String, Integer> map) {
		 int result = session.delete(NAMESPACE + "removeReplyIfNoChildReplies", map);
		 return result > 0 ? 1 : 0;
	}

	@Override
	public int updateCommentStep(Map<String, Integer> map) {
		return session.update(NAMESPACE + "updateCommentStep", map);
	}

	@Override
	public void createTag(TagDto tag) {
		session.insert(NAMESPACE + "createTag", tag);
	}

	@Override
	public int existsByTagName(String tagName) {
		return session.selectOne(NAMESPACE + "existsByTagName", tagName);
	}

	@Override
	public void addTagToPost(PostTagDto postTagDto) {
		session.insert(NAMESPACE+ "addTagToPost", postTagDto);
	}

	@Override
	public List<TagDto> findTagsByPostId(int bId) {
		return session.selectList(NAMESPACE + "findTagsByPostId", bId);
	}

	@Override
	public int findTagIdByName(String tagName) {
		return session.selectOne(NAMESPACE + "findTagIdByName", tagName);
	}

	@Override
	public void deleteTagsByPostId(int bId) {
		session.delete(NAMESPACE + "deleteTagsByPostId", bId);
	}

	@Override
	public List<TagDto> getAllTags() {
		return session.selectList(NAMESPACE + "getAllTags");
	}

	@Override
	public int getCommentOrderById(Map<String, Object> map) {
		return session.selectOne(NAMESPACE + "getCommentOrderById", map);
	}

}
