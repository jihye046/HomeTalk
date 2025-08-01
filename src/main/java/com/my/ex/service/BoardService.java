package com.my.ex.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.ex.dao.BoardDao;
import com.my.ex.dto.BoardDto;
import com.my.ex.dto.BoardPagingDto;
import com.my.ex.dto.CommentsPagingDto;
import com.my.ex.dto.NotificationDto;
import com.my.ex.dto.PostTagDto;
import com.my.ex.dto.TagDto;

@Service
public class BoardService implements IBoardService {

	// BETWEEN A AND B
	private static final int PAGE_LIMIT = 12;  // 한 페이지에 표시할 게시글 개수
    private static final int BLOCK_LIMIT = 5;  // 하단에 표시할 페이지 번호 블록 개수 [1],[2] ..
    private static final int COMMENTS_PAGE_LIMIT = 6;  // 한 페이지에 표시할 댓글 개수
    private static final int COMMENTS_BLOCK_LIMIT = 3;  // 하단에 표시할 페이지 번호 블록 개수 [1],[2] ..
	
	@Autowired
	private BoardDao dao;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public boolean createBoard(BoardDto dto) {
		int result = dao.createBoard(dto);
		return (result > 0) ? true : false;
	}

	@Override
	public List<BoardDto> findAll() {
		return dao.findAll();
	}

	@Override
	public BoardDto detailBoard(int bId) {
		return dao.detailBoard(bId);
	}

	@Override
	public boolean updateBoard(BoardDto dto) {
		int result = dao.updateBoard(dto);
		return (result > 0) ? true : false;
	}

	@Override
	public boolean deleteBoard(int bId) {
		int result = dao.deleteBoard(bId);
		return (result > 0) ? true : false;
	}

	@Override
	public void updateHitCount(int bId) {
		dao.updateHitCount(bId);
	}
	
	@Override
	public int incrementLikesCount(int bId) {
		return dao.incrementLikesCount(bId);
	}
	
	@Override
	public int decrementLikesCount(int bId) {
		return dao.decrementLikesCount(bId);
	}
	
	@Override
	public int getTotalLikes(int bId) {
		return dao.getTotalLikes(bId);
	}
	
	@Override
	public int incrementRecommendationAndGetCount(int bId) {
		dao.incrementRecommendation(bId);
		return getTotalLikes(bId);
	}

	@Override
	public int decrementRecommendationAndGetCount(int bId) {
		dao.decrementRecommendation(bId);
		return getTotalLikes(bId);
	}
	
	@Override
	public int incrementBookmarkAndGetCount(int bId) {
		dao.incrementBookmarksCount(bId); 
		return dao.getTotalBookmarks(bId);
	}

	@Override
	public int decrementBookmarkAndGetCount(int bId) {
		dao.decrementBookmarksCount(bId);
		return dao.getTotalBookmarks(bId);
	}
	
	@Override
	public int getTotalBookmarks(int bId) {
		return dao.getTotalBookmarks(bId);
	}

	@Override
	public void replyInsert(BoardDto dto, NotificationDto notificationDto, int commentPage) {
		dao.replyInsert(dto);
		
		// 댓글 작성자와 게시글 작성자가 다를 경우에만 알림 생성
		if(!notificationDto.getUserId().equals(notificationDto.getSenderId())) {
			int commentId = dto.getbId(); // 댓글 삽입 후 set된 댓글ID
			
			String commentSnippet = dto.getbContent().length() > 10 ? dto.getbContent().substring(0, 10) + "..." : dto.getbContent();
			ObjectMapper mapper = new ObjectMapper();
			String dataJsonString = null;
			try {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("commentSnippet", commentSnippet);
				dataMap.put("commentPage", commentPage);
				dataJsonString = mapper.writeValueAsString(dataMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			notificationDto.setDataJson(dataJsonString);
			
			notificationDto.setRelatedId(commentId);
			notificationDto.setLink("/board/detailBoard?"
					+ "bId=" + dto.getbGroup()
					+ "&bGroup=" + dto.getbGroup() 
					+ "&bName=" + notificationDto.getUserId() // 알림을 받을 사용자ID(게시글 작성자ID)
					+ "&targetCommentId=" + commentId);
			notificationService.addNotification(notificationDto);
		}
	}
	
	@Override
	public int calculateCommentPage(Integer targetCommentId, int bGroup, int commentsPageLimit) {
		// targetComment가 몇번째에 위치하는지 확인
		Map<String, Object> map = new HashMap<>();
		map.put("targetCommentId", targetCommentId);
		map.put("bGroup", bGroup);
		int commentOrder = dao.getCommentOrderById(map);
		
		// targetComment가 위치한 페이지 링크
		int calculatedPage =(int) Math.ceil((double)commentOrder / COMMENTS_PAGE_LIMIT);
		return calculatedPage;
	}

	@Override
	public List<BoardDto> replyList(int bGroup) {
		return dao.replyList(bGroup);
	}

	@Override
	public void replyChildInsert(BoardDto dto, NotificationDto notificationDto, int commentPage) {
		dao.replyChildInsert(dto);
		
		// 답글 작성자와 댓글 작성자가 다를 경우에만 알림 생성
		if(!notificationDto.getUserId().equals(notificationDto.getSenderId())) {
			int childCommentId = dto.getbId(); // 답글 삽입 후 set된 답글ID
			
			String childCommentSnippet = dto.getbContent().length() > 10 ? dto.getbContent().substring(0, 10) + "..." : dto.getbContent();
			ObjectMapper mapper = new ObjectMapper();
			String dataJson = null;
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("childcommentPage", "1");
				map.put("childcommentSnippet", childCommentSnippet);
				dataJson = mapper.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			notificationDto.setDataJson(dataJson);
			
			notificationDto.setRelatedId(childCommentId);
			notificationDto.setLink("/board/detailBoard?"
					+ "bId=" + dto.getbGroup()
					+ "&bGroup=" + dto.getbGroup() 
					+ "&bName=" + notificationDto.getUserId() // 알림을 받을 사용자ID(게시글 작성자ID)
					+ "&targetCommentId=" + childCommentId);
			notificationService.addNotification(notificationDto);
		}
	}

	@Override
	public List<BoardDto> replyChildList(int bGruop) {
		return null;
	}

	// 게시글 목록 가져오기
	@Override
	public List<BoardDto> pagingList(int page, String searchGubun, String searchText, String sortType) {
		int pagingEnd = page * PAGE_LIMIT;
		int pagingStart = pagingEnd - (PAGE_LIMIT - 1);
		HashMap<String, Object> map = new HashMap<>();
		map.put("pagingStart", pagingStart);
		map.put("pagingEnd", pagingEnd);
		map.put("searchGubun", searchGubun);
		map.put("searchText", searchText);
		map.put("sortType", sortType);
		return dao.pagingList(map);
	}
	
	// 게시글 조회순 정렬
	@Override
	public List<BoardDto> sort_hitPagingList(int page, String searchGubun, String searchText) {
		int pagingEnd = page * PAGE_LIMIT;
		int pagingStart = pagingEnd - (PAGE_LIMIT - 1);
		HashMap<String, Object> map = new HashMap<>();
		map.put("pagingStart", pagingStart);
		map.put("pagingEnd", pagingEnd);
		map.put("searchGubun", searchGubun);
		map.put("searchText", searchText);
		return dao.sort_hitPagingList(map);
	}

	// 게시글 페이지 하단 번호 블록
	@Override
	public BoardPagingDto pagingParam(int page) {
		// 전체 글 개수
		int boardCount = dao.boardCount();
		
		// 전체 블록 개수 
		int maxPage = (int)(Math.ceil((double) boardCount / PAGE_LIMIT));
		
		// 시작 블록 값
		int startPage = (((int)(Math.ceil((double) page / BLOCK_LIMIT))) - 1) * BLOCK_LIMIT + 1;
		
		// 끝 블록 값
		int endPage = startPage + BLOCK_LIMIT - 1;
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		BoardPagingDto pageDto = new BoardPagingDto();
		pageDto.setPage(page);
		pageDto.setMaxPage(maxPage);
		pageDto.setStartPage(startPage);
		pageDto.setEndPage(endPage);
		return pageDto;
	}
	
	// 댓글 목록 가져오기
	@Override
	public List<BoardDto> commentsPagingList(int page, String sortType, int bGroup) {
		int pagingEnd = page * COMMENTS_PAGE_LIMIT;
		int pagingStart = pagingEnd - (COMMENTS_PAGE_LIMIT - 1) ; 
		HashMap<String, Object> map = new HashMap<>();
		map.put("commentsPagingStart", pagingStart);
		map.put("commentsPagingEnd", pagingEnd);
		map.put("commentsSortType", sortType);
		map.put("bGroup", bGroup);
 		return dao.commentsPagingList(map);
	}
	
	// 댓글 페이지 하단 번호 블록
	@Override
	public CommentsPagingDto commentsPagingParam(int page, int bGroup) {
		// 전체 댓글 개수
//		int boardCount = dao.commentsCount(bGroup);
		int boardCount = dao.commentsCount(bGroup);
		
		// 전체 블록 개수 
		int maxPage = (int)(Math.ceil((double) boardCount / COMMENTS_PAGE_LIMIT));
		
		// 시작 블록 값
		int startPage = (((int)(Math.ceil((double) page / COMMENTS_BLOCK_LIMIT))) - 1) * COMMENTS_BLOCK_LIMIT + 1;
		
		// 끝 블록 값
		int endPage = startPage + COMMENTS_BLOCK_LIMIT - 1;
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		CommentsPagingDto commentsPageDto = new CommentsPagingDto();
		commentsPageDto.setPage(page);
		commentsPageDto.setMaxPage(maxPage);
		commentsPageDto.setStartPage(startPage);
		commentsPageDto.setEndPage(endPage);
		return commentsPageDto;
	}

	@Override
	public int incrementCommentCount(int bGroup) {
		return dao.incrementCommentCount(bGroup);
	}

	@Override
	public int decrementCommentCount(int bGroup) {
		return dao.decrementCommentCount(bGroup);
	}

	@Override
	public int commentsCount(int bGroup) {
		return dao.commentsCount(bGroup);
	}

	@Override
	public boolean removeReply(Map<String, Object> map) {
		return dao.removeReply(map) > 0;
	}

	@Override
	public boolean removeReplyIfNoChildReplies(Map<String, Integer> map) {
		int result = dao.removeReplyIfNoChildReplies(map);
		return result == 1 ? true : false;
	}

	@Override
	public boolean updateCommentStep(Map<String, Integer> map) {
		return dao.updateCommentStep(map) > 0;
	}

	@Override
	public void createTag(int bId, List<TagDto> tags) {
		for(TagDto tag : tags) {
			int tagId;
			
			if(!existsByTagName(tag.getTagName())) { // 새로 등록할 태그가 중복이 아닌 경우만 등록
				dao.createTag(tag);
				tagId = tag.getTagId();
			} else {
				tagId = dao.findTagIdByName(tag.getTagName());
			}
			
			PostTagDto postTagDto = new PostTagDto();
			postTagDto.setBId(bId);
			postTagDto.setTagId(tagId);
			dao.addTagToPost(postTagDto);
		}
	}

	@Override
	public boolean existsByTagName(String tagName) {
		int result = dao.existsByTagName(tagName);
		return result > 0; 
	}

	@Override
	public List<TagDto> findTagsByPostId(int bId) {
		return dao.findTagsByPostId(bId);
	}

	@Override
	public void updateTag(int bId, List<TagDto> tags) {
		dao.deleteTagsByPostId(bId);
		
		for(TagDto tag : tags) {
			int tagId;
			
			if(!existsByTagName(tag.getTagName())) { // 새로 등록할 태그가 중복이 아닌 경우만 등록
				dao.createTag(tag);
				tagId = tag.getTagId();
			} else {
				tagId = dao.findTagIdByName(tag.getTagName());
			}
			
			PostTagDto postTagDto = new PostTagDto();
			postTagDto.setBId(bId);
			postTagDto.setTagId(tagId);
			dao.addTagToPost(postTagDto);
		}
	}
	
	@Override
	public void deleteTagsByPostId(int bId) {
		dao.deleteTagsByPostId(bId);
	}

	@Override
	public List<TagDto> getAllTags() {
		return dao.getAllTags();
	}

}
