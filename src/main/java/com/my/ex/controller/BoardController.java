package com.my.ex.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.ex.CommentsListResponse;
import com.my.ex.EnvironmentService;
import com.my.ex.SortResponse;
import com.my.ex.dto.BoardDto;
import com.my.ex.dto.BoardPagingDto;
import com.my.ex.dto.BookmarkDto;
import com.my.ex.dto.CommentsPagingDto;
import com.my.ex.dto.LikeDto;
import com.my.ex.dto.TagDto;
import com.my.ex.dto.map.KakaoMapRequestDto;
import com.my.ex.service.BoardService;
import com.my.ex.service.BookmarkService;
import com.my.ex.service.LikeService;
import com.my.ex.service.SocialService;
import com.my.ex.service.UserService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SocialService socialuserService;
	
	@Autowired
	private KakaoMapRequestDto kakao;
	
	@Autowired
	private EnvironmentService environmentService;
	
	// 게시글 등록 페이지
	@RequestMapping("/createPage")
	public String createPage(Model model) throws JsonProcessingException {
		model.addAttribute("jsKey", kakao.getJsKey());
		
		// 자동완성 태그 목록
		List<TagDto> allTagList = service.getAllTags();
		ObjectMapper mapper = new ObjectMapper();
		String allTagJsonList = mapper.writeValueAsString(allTagList);
		model.addAttribute("allTagJsonList", allTagJsonList);
		
		// 파일 업로드 요청 url
		model.addAttribute("initRequestUrl", environmentService.getInitRequest());
		
		return "/board/createPage";
	}
	
	// 게시글 등록
	@RequestMapping(value = "/createBoard", method = RequestMethod.POST )
	public String createBoard(BoardDto dto, RedirectAttributes rttr) throws JsonParseException, JsonMappingException, IOException {
		// 게시글 생성
		boolean create = service.createBoard(dto);
		rttr.addFlashAttribute("createResult", create ? "true" : "false");
		
		// 태그 생성
		ObjectMapper mapper = new ObjectMapper();
		if(dto.getTags() != null && dto.getTags() != "") {
			// JSON 형태의 태그 문자열을 객체 리스트로 변환하여 저장
			List<TagDto> tags = mapper.readValue(dto.getTags(), new TypeReference<List<TagDto>>() {});
			service.createTag(dto.getbId(), tags);
		}
		
		return "redirect:paging";
	}
	
	
	// 게시글 상세보기
	@RequestMapping("/detailBoard")
	public String detailBoard(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
		HttpServletRequest request,
		Model model, 
		HttpSession session, 
		@RequestParam int bId, 
		@RequestParam int bGroup,
		@RequestParam String bName) {
		
		String userId = (String)session.getAttribute("userId"); 
		
		// 게시글 조회
		BoardDto dto = service.detailBoard(bId);
		// 해당 게시글에 사용자가 좋아요를 눌렀는지 확인
		boolean isLiked = likeService.isLiked(bId, userId);
		// 해당 게시글에 사용자가 북마크를 눌렀는지 확인
		boolean isBookmarked = bookmarkService.isBookmarked(bId, userId);
		// 댓글 페이징 정보
		commentsPaging(page, sortType, bGroup, model, userId);
		// 좋아요 수 업데이트
		updateHitCount(bId);
		// 프로필 이미지 url 반환
		String filename = userService.getProfileFilename(bName);
				
		String imageUrl = "/user/getProfileImage/" + filename;
		// 태그 저장
		List<TagDto> tagList = service.findTagsByPostId(bId);
		
		model.addAttribute("dto", dto);
		model.addAttribute("isLiked", isLiked);
		model.addAttribute("isBookmarked", isBookmarked);
		model.addAttribute("imageUrl", imageUrl);
		model.addAttribute("jsKey", kakao.getJsKey());
		model.addAttribute("tagList", tagList);
		return "/board/detailPage";
	}
	
	// 게시글 수정 페이지
	@RequestMapping("/updatePage")
	public String updatePage(@RequestParam("bId") int bId, Model model) throws JsonProcessingException {
		// 게시글
		BoardDto dto = service.detailBoard(bId);
		model.addAttribute("dto", dto);
		
		ObjectMapper mapper = new ObjectMapper();
		
		// 게시글에 등록되어있던 태그 목록
		List<TagDto> tagList = service.findTagsByPostId(bId);
		String tagJsonList = mapper.writeValueAsString(tagList);
		model.addAttribute("tagJsonList", tagJsonList);
		
		// 자동완성 태그 목록
		List<TagDto> allTagList = service.getAllTags();
		String allTagJsonList = mapper.writeValueAsString(allTagList);
		model.addAttribute("allTagJsonList", allTagJsonList);
		
		// 파일 업로드 요청 url
		model.addAttribute("initRequestUrl", environmentService.getInitRequest());
		
		return "/board/updatePage";
	}
	
	// 게시글 수정
	@RequestMapping(value = "/updateBoard", method = RequestMethod.POST)
	public String updateBoard(HttpServletRequest request, @ModelAttribute BoardDto dto, RedirectAttributes rttr) throws JsonParseException, JsonMappingException, IOException {
		// 게시글 수정
		boolean update = service.updateBoard(dto);
		rttr.addFlashAttribute("updateResult", update ? "true" : "false");
		
		// 태그 수정
		ObjectMapper mapper = new ObjectMapper();
		if(dto.getTags() != null && dto.getTags() != "") {
			List<TagDto> tags = mapper.readValue(dto.getTags(), new TypeReference<List<TagDto>>() {});
			service.updateTag(dto.getbId(), tags);
		}
		
		return "redirect:detailBoard?bId=" + dto.getbId() + "&bGroup=" + dto.getbGroup() + "&bName=" + dto.getbName();
	}
	
	// 게시글 삭제
	@RequestMapping("/deleteBoard")
	public String deleteBoard(@RequestParam("bId")int bId, RedirectAttributes rttr) {
		service.deleteTagsByPostId(bId);
		boolean isBoardDeleted = service.deleteBoard(bId);
		rttr.addFlashAttribute("deleteResult", isBoardDeleted ? "true" : "false");
		
		return "redirect:/board/paging";
	}
	
	// 조회수
	@RequestMapping("/updateHitCount")
	public void updateHitCount(@RequestParam("bId")int bId) {
		service.updateHitCount(bId);
	}
	
	// 게시글 좋아요
	@ResponseBody
	@RequestMapping(value =  "/addLike", method = RequestMethod.POST)
	public ResponseEntity<Integer> addLike(@RequestParam("bId")int bId, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		service.incrementLikesCount(bId);
		LikeDto dto = new LikeDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		likeService.addLike(dto);
		int totalLikes = service.getTotalLikes(bId);
		return new ResponseEntity<>(totalLikes, HttpStatus.OK);
	}
	
	// 게시글 좋아요 취소
	@ResponseBody
	@RequestMapping(value =  "/removeLike", method = RequestMethod.POST)
	public ResponseEntity<Integer> removeLike(@RequestParam("bId")int bId, HttpSession session) {
		String userId = (String)session.getAttribute("userId");
		service.decrementLikesCount(bId);
		LikeDto dto = new LikeDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		likeService.removeLike(dto);
		int totalLikes = service.getTotalLikes(bId);
		return new ResponseEntity<>(totalLikes, HttpStatus.OK);
	}
	
	// 게시글 북마크
	@ResponseBody
	@RequestMapping(value = "/addBookmark", method = RequestMethod.POST)
	public Map<String, Integer> addBookmark(@RequestBody Map<String, String> request, HttpSession session) {
		int bId = Integer.parseInt(request.get("bId"));
		String userId = UserController.getUserIdFromSession(session);
		
		Map<String, Integer> response = new HashMap<>();
		BookmarkDto dto = new BookmarkDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		bookmarkService.addBookmark(dto);
		
		int bookmarkCount = service.incrementBookmarkAndGetCount(bId);
		response.put("bookmarkCount", bookmarkCount);
		return response;
	}
	
	// 게시글 북마크 해제
	@ResponseBody
	@RequestMapping(value = "/removeBookmark", method = RequestMethod.POST)
	public Map<String, Integer> removeBookmark(@RequestBody Map<String, String> request, HttpSession session) {
		int bId = Integer.parseInt(request.get("bId"));
		String userId = UserController.getUserIdFromSession(session);
		
		BookmarkDto dto = new BookmarkDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		bookmarkService.removeBookmark(dto);
		
		Map<String, Integer> response = new HashMap<>();
		int bookmarkCount = service.decrementBookmarkAndGetCount(bId);
		response.put("bookmarkCount", bookmarkCount);
		return response;
	}
	
	// 댓글 추천
	@ResponseBody
	@RequestMapping(value = "/addRecommend", method = RequestMethod.POST)
	public ResponseEntity<Integer> addRecommend(@RequestBody Map<String, String> request, HttpSession session) {
		int bId = Integer.parseInt(request.get("bId"));
		String userId = UserController.getUserIdFromSession(session);
		
		LikeDto dto = new LikeDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		
		likeService.addRecommend(dto);
		int totalRecommendations = service.incrementRecommendationAndGetCount(bId);
		return new ResponseEntity<>(totalRecommendations, HttpStatus.OK);
	}
	
	// 댓글 추천 취소
	@ResponseBody
	@RequestMapping(value = "/removeRecommend", method = RequestMethod.POST)
	public ResponseEntity<Integer> removeRecommend(@RequestBody Map<String, String> request, HttpSession session) {
		int bId = Integer.parseInt(request.get("bId"));
		String userId = UserController.getUserIdFromSession(session);
		
		LikeDto dto = new LikeDto();
		dto.setBId(bId);
		dto.setUserId(userId);
		
		likeService.removeRecommend(dto);
		int totalRecommendations = service.decrementRecommendationAndGetCount(bId);
		return new ResponseEntity<>(totalRecommendations, HttpStatus.OK);
	}
	
	// 댓글 등록
	@ResponseBody
	@RequestMapping(value = "/replyInsert", method = RequestMethod.POST)
	public Map<String, Object> replyInsert(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
										   @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
										   HttpSession session,
										   BoardDto dto) {
		String userId = (String)session.getAttribute("userId");
		dto.setbName(userId);
		service.replyInsert(dto);
		
		int commentsCount = service.incrementCommentCount(dto.getbGroup());
		CommentsListResponse response = commentsPagingAjax(page, sortType, dto.getbGroup(), session);
		Map<String, Object> map = new HashMap<>();
		map.put("commentsCount", commentsCount);
		map.put("commentsListResponse", response);
		
		return map;
	}
	
	// 답글 등록
	@ResponseBody
	@RequestMapping(value = "/replyChildInsert", method = RequestMethod.POST)
	public Map<String, Object> replyChildInsert(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
										  		@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
										  		HttpSession session, BoardDto dto) {
		String userId = (String)session.getAttribute("userId");
		dto.setbName(userId);
		service.replyChildInsert(dto);
		
		int commentsCount = service.incrementCommentCount(dto.getbGroup());
		CommentsListResponse response = commentsPagingAjax(page, sortType, dto.getbGroup(), session);
		Map<String, Object> map = new HashMap<>();
		map.put("commentsCount", commentsCount);
		map.put("commentsListResponse", response);
		return map;
	}
	
	// 댓글 삭제
	@ResponseBody
	@RequestMapping(value = "/removeReply", method = RequestMethod.POST)
	public Map<String, Object> removeReply(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
										   @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
										   @RequestParam(value = "bGroup") int bGroup,
										   @RequestParam(value = "bStep") int bStep,
										   @RequestParam(value = "bIndent") int bIndent,
										   @RequestParam("bId") int bId, 
										   HttpSession session){
		
		Map<String, Object> map = new HashMap<>();
		map.put("bGroup", bGroup);
		map.put("bStep", bStep);
		map.put("bIndent", bIndent);
		map.put("bId", bId);
		
		String msg = "";
		int commentsCount = 0;
		if(service.removeReply(map)) {
			commentsCount = service.decrementCommentCount(bGroup);
			msg = "댓글이 삭제되었습니다.";
		} else {
			msg = "알 수 없는 오류가 발생했습니다.";
		}
		
		CommentsListResponse commentListResponse = commentsPagingAjax(page, sortType, bGroup, session);
		
		Map<String, Object> response = new HashMap<>();
		response.put("commentsListResponse", commentListResponse);
		response.put("commentsCount", commentsCount);
		response.put("msg", msg);
		
		return response;
	}
	
	// 답글 삭제
	@ResponseBody
	@RequestMapping(value = "/removeChildReply", method = RequestMethod.POST)
	public Map<String, Object> removeChildReply(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
										   		@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
									   			@RequestParam(value = "bGroup") int bGroup,
									   			@RequestParam("bId") int bId,
									   			@RequestParam("bStep") int bStep,
									   			HttpSession session){
		
		String msg = "";
		int commentsCount = 0; 
		CommentsListResponse commentListResponse = null;
		if(service.deleteBoard(bId)) {
			commentsCount = service.decrementCommentCount(bGroup);
			
			// 답글 삭제 후 다른 답글이 더 있다면 bStep을 1씩 줄임 
			Map<String, Integer> map = new HashMap<>();
			map.put("bGroup", bGroup);
			map.put("bStep", bStep);
			boolean updateCommentStepResult = service.updateCommentStep(map);
//			if(!updateCommentStepResult) {
				// 답글이 없는 삭제된 댓글이라면 delete를 함
				service.removeReplyIfNoChildReplies(map);
//			}
			commentListResponse = commentsPagingAjax(page, sortType, bGroup, session);
			msg = "답글이 삭제되었습니다.";
		} else {
			msg = "알 수 없는 오류가 발생했습니다.";
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("commentsListResponse", commentListResponse);
		response.put("commentsCount", commentsCount);
		response.put("msg", msg);
		
		return response;
	}
	
	// 정렬 유지한채로 페이지 이동 시 -> jsp(html)로 이동
	@RequestMapping("/paging")
	public String paging(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
									  @RequestParam(value = "searchGubun", required = false, defaultValue = "") String searchGubun,
									  @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
									  @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType) {
		List<BoardDto> pagingList = service.pagingList(page, searchGubun, searchText, sortType);
		BoardPagingDto pageDto = service.pagingParam(page);
		for(BoardDto dto : pagingList) {
			dto.setSearchGubun(searchGubun);
			dto.setSearchText(searchText);
			dto.setSortType(sortType);
			
			// HTML 이스케이프 처리
			String escapedContent = HtmlUtils.htmlEscape(dto.getbContent());
			dto.setbContent(escapedContent);
		}
		model.addAttribute("boardList", pagingList);
		model.addAttribute("paging", pageDto);
		return "/board/pagingList";
	}
	
	// 현재 페이지에서 정렬 시 -> js(ajax)로 이동
	@ResponseBody
	@RequestMapping("/paging/ajax")
	public ResponseEntity<SortResponse> pagingAjax(Model model,
										@RequestParam(value = "page", required = false, defaultValue = "1") int page,
										@RequestParam(value = "searchGubun", required = false, defaultValue = "") String searchGubun,
										@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
										@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType) {
		List<BoardDto> pagingList = service.pagingList(page, searchGubun, searchText, sortType);
		BoardPagingDto pageDto = service.pagingParam(page);
		for(BoardDto dto : pagingList) {
			dto.setSearchGubun(searchGubun);
			dto.setSearchText(searchText);
			dto.setSortType(sortType);
			
			// HTML 이스케이프 처리
			String escapedContent = HtmlUtils.htmlEscape(dto.getbContent());
			dto.setbContent(escapedContent);
		}
		SortResponse response = new SortResponse(pagingList, pageDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// 조회순 정렬
	@ResponseBody
	@RequestMapping("/sort_hit")
	public ResponseEntity<SortResponse> sort_hit(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
												 @RequestParam(value = "searchGubun", required = false, defaultValue = "") String searchGubun,
										 		 @RequestParam(value= "searchText", required = false, defaultValue = "") String searchText) {
		List<BoardDto> sort_hitPagingList = service.sort_hitPagingList(page, searchGubun, searchText);
		BoardPagingDto pageDto = service.pagingParam(page);
		for(BoardDto dto : sort_hitPagingList) {
			// HTML 이스케이프 처리
			String escapedContent = HtmlUtils.htmlEscape(dto.getbContent());
			dto.setbContent(escapedContent);
		}
		SortResponse response = new SortResponse();
		response.setSort_hitPagingList(sort_hitPagingList);
		response.setPageDto(pageDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// 댓글 페이징_로드
	public Model commentsPaging(int page, String sortType, int bGroup, Model model, String userId) {
		// 댓글 목록
		List<BoardDto> commentsPagingList = service.commentsPagingList(page, sortType, bGroup);
		
		// 프로필 이미지 목록
		List<String> profileImageUrls = new ArrayList<>();
		for(BoardDto dto : commentsPagingList) {
			String filename = userService.getProfileFilename(dto.getbName()); // 파일 이름
			String imageUrl = "/user/getProfileImage/" + filename;
			profileImageUrls.add(imageUrl);
		}
		
		CommentsPagingDto commentsPageDto = service.commentsPagingParam(page, bGroup);
		for(BoardDto dto : commentsPagingList) {
			dto.setSortType(sortType);
			// HTML 이스케이프 처리
			String escapedContent = HtmlUtils.htmlEscape(dto.getbContent());
			dto.setbContent(escapedContent);
			
			boolean isRecommended = likeService.isRecommended(dto.getbId(), userId);
			dto.setRecommended(isRecommended);
		}
		model.addAttribute("commentsPagingList", commentsPagingList);
		model.addAttribute("commentsPaging", commentsPageDto);
		model.addAttribute("profileImageUrls", profileImageUrls);
		return model;
	}
	
	// 댓글 페이징_비동기
	@ResponseBody
	@RequestMapping("/commentsPaging/ajax")
	public CommentsListResponse commentsPagingAjax(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
												   @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
												   int bGroup,
												   HttpSession session) {
		// 'page', 'sortType' 값이 없는 경우 지정해주어야하는데, fetch로 보내면 @RequestParam을 사용못하기때문에 $.ajax로 요청하고 @RequestParam을 사용 
		List<BoardDto> commentsPagingList = service.commentsPagingList(page, sortType, bGroup);
		
		// 프로필 이미지 목록
		List<String> profileImageUrls = new ArrayList<>();
		for(BoardDto dto : commentsPagingList) {
			String filename = userService.getProfileFilename(dto.getbName()); // 파일 이름
			String imageUrl = "/user/getProfileImage/" + filename;
			profileImageUrls.add(imageUrl);
		}
		
		CommentsPagingDto commentsPageDto = service.commentsPagingParam(page, bGroup);
		String userId = UserController.getUserIdFromSession(session);
		for(BoardDto dto : commentsPagingList) {
			dto.setSortType(sortType);
			// HTML 이스케이프 처리
			String escapedContent = HtmlUtils.htmlEscape(dto.getbContent());
			dto.setbContent(escapedContent);
			
			boolean isRecommended = likeService.isRecommended(dto.getbId(), userId);
			dto.setRecommended(isRecommended);
		}
		CommentsListResponse response = new CommentsListResponse(commentsPagingList, commentsPageDto, profileImageUrls);
		return response;
	}
	
	// 이미지업로드(1) - 업로드한 이미지를 로컬에 저장
	@ResponseBody
	@RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
	public void imgUpload(MultipartHttpServletRequest multiRequest, 
						  HttpServletRequest request,
						  HttpServletResponse response) {
		try {
			String real_save_path = environmentService.getUploadPath();
			
            // 폴더가 없을 경우 생성
            File saveFolder = new File(real_save_path);
            if (!saveFolder.exists() || saveFolder.isFile()) {
                saveFolder.mkdirs();
            }

            final Map<String, MultipartFile> files = multiRequest.getFileMap();
            MultipartFile fileload = files.get("upload");
            if (fileload == null || fileload.isEmpty()) {
                System.out.println("파일이 없습니다.");
                return;
            }
            
            // filename 취득
            String fileName = fileload.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            Random ran = new Random(System.currentTimeMillis());
            fileName = System.currentTimeMillis() + "_" + (int)(ran.nextDouble() * 10000) + "." + ext;

            // 폴더 경로 설정
            String newfilename = real_save_path + fileName;
            fileload.transferTo(new File(newfilename)); // 파일 저장 실행
            
            String imageUrl = environmentService.getAccessPath() + fileName;
            
            // getImageForContents() 메서드 실행
            JSONObject outData = new JSONObject();
            outData.put("uploaded", true);
            outData.put("url", imageUrl);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(outData.toString());
        } catch (Exception e) {
        	System.out.println("*error: " + e.getMessage());
            e.printStackTrace();
        }
		
	}
	
	// 이미지업로드(2) - 로컬에 저장된 이미지를 사용자에게 제공(텍스트창)
	@RequestMapping(value = "/getImageForContents")
    public void getImageForContents(@RequestParam("fileNm") String fileNm, HttpServletResponse response) throws Exception {
//        String fileStr = "C:\\server_program\\imgUploadTest\\"; 
        String fileStr = environmentService.getUploadPath();
        
        FileInputStream fis = null;
        BufferedInputStream in = null;
        ByteArrayOutputStream bStream = null;

        try {
            File file = new File(fileStr, fileNm);
            if (!file.exists()) { 
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 파일이 없으면 404 오류 응답
                return;
            }

            fis = new FileInputStream(file);
            in = new BufferedInputStream(fis);
            bStream = new ByteArrayOutputStream();

            int imgByte;
            while ((imgByte = in.read()) != -1) {
                bStream.write(imgByte);
            }

            String type = "";
            String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1).toLowerCase();
            type = "jpg".equals(ext) ? "image/jpeg" : "image/" + ext;
            System.out.println(type);
            response.setHeader("Content-Type", type);
            response.setContentLength(bStream.size());
            bStream.writeTo(response.getOutputStream());
            response.getOutputStream().flush();
        } finally {
            // 자원 해제
            if (bStream != null) bStream.close();
            if (in != null) in.close();
            if (fis != null) fis.close();
        }
    }
	
	// 카카오 key
	@ResponseBody
	@RequestMapping(value =  "/getKakaoKey", method = RequestMethod.POST)
	public ResponseEntity<String> getKakaoKey() {
		return new ResponseEntity<>(kakao.getJsKey(), HttpStatus.OK);
	}
	
}
