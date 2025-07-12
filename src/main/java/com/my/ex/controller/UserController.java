package com.my.ex.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.ex.EnvironmentService;
import com.my.ex.dto.BoardDto;
import com.my.ex.dto.UserDto;
import com.my.ex.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private EnvironmentService environmentService;
	
	// 세션에 저장된 userId 가져오기
	public static String getUserIdFromSession(HttpSession session) {
	    return (String) session.getAttribute("userId");
	}
	
	// 로그인 페이지
	@RequestMapping("/loginPage")
	public String loginPage() {
		return "/user/loginPage";
	}
	
	// 로그인
	// 로그인 전 위치로 이동
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) {
		String userId = request.getParameter("userId");
		
		String userPw = request.getParameter("userPw");
		String encodePassword = service.getUserPassword(userId);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// 비밀번호 일치 여부 확인
		boolean isPasswordMatch = passwordEncoder.matches(userPw, encodePassword);
		System.out.println(isPasswordMatch);
		
//		boolean loginResult = service.login(userId, userPw);
		if(isPasswordMatch) {
			session.setAttribute("userId", userId);
			String userNickname = service.getUserNickname(userId);
			session.setAttribute("userNickname", userNickname);
			String targetLocation = (String)session.getAttribute("targetLocation");
			return (targetLocation != null) ? "redirect:" + targetLocation : "redirect:/board/paging";
		} else {
			rttr.addFlashAttribute("loginFail", true);
			return "redirect:loginPage";
		}
	}
	
	// 로그아웃
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 자체를 삭제
		return "redirect:loginPage";
	}
	
	// 회원가입 페이지
	@RequestMapping("/joinPage")
	public String joinPage() {
		return "/user/joinPage";
	}
	
	// 회원가입
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(UserDto dto, RedirectAttributes rttr, HttpSession session) {
		boolean isPasswordValid = isPasswordValid(dto.getUserPw());
		boolean isIdValid = isIdValid(dto.getUserId());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(isPasswordValid && isIdValid) {
			String hashedPassword = passwordEncoder.encode(dto.getUserPw());
			dto.setUserPw(hashedPassword);
			boolean joinResult = service.join(dto);
			
			if(joinResult) {
				rttr.addFlashAttribute("userId", dto.getUserId());
				rttr.addFlashAttribute("joinResult", true);
			}
			return "redirect:loginPage";
		}
		
		rttr.addFlashAttribute("joinResult", false);
		return "redirect:loginPage";
	}

	// 마이페이지
	@RequestMapping("/myPage")
	public String myPage() {
		return "/user/myPage";
	}
	
	// 내가 작성한 게시글 (동기)
	@RequestMapping("/getUserPosts")
	public String getUserPosts(HttpSession session, 
			Model model, 
			@RequestParam(value = "searchGubun", required = false, defaultValue = "") String searchGubun,
			@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
			@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType) {
		String userId = getUserIdFromSession(session);
		
		List<BoardDto> list = service.getUserPosts(userId, searchGubun, searchText, sortType);
 		model.addAttribute("getUserPosts", list);
		return "/user/getUserPosts";
	}
	
	// 내가 작성한 게시글 (비동기)
	@ResponseBody
	@RequestMapping("/getUserPosts/axios")
	public List<BoardDto> getUserPostsAxios(HttpSession session, 
			Model model, 
			@RequestParam(value = "searchGubun", required = false, defaultValue = "") String searchGubun,
			@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
			@RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType) {
		String userId = getUserIdFromSession(session);
		List<BoardDto> list = service.getUserPosts(userId, searchGubun, searchText, sortType);
		return list;
	}
	
	// 내가 작성한 댓글
	@RequestMapping("/getUserComments")
	public String getUserComments(HttpSession session, Model model) {
		String userId = getUserIdFromSession(session);
		List<BoardDto> list = service.getUserComments(userId);
		model.addAttribute("getUserComments", list);
		return "/user/getUserComments";
	}
	
	// 좋아요한 게시글
	@RequestMapping("/getUserLikedPosts")
	public String getUserLikedPosts(HttpSession session, Model model) {
		String userId = getUserIdFromSession(session);
		List<BoardDto> list = service.getUserLikedPosts(userId);
		model.addAttribute("getUserLikedPosts", list);
		return "/user/getUserLikedPosts";
	}
	
	// 비밀번호 변경 페이지
	@RequestMapping("/changePasswordForm")
	public String changePasswordForm(HttpSession session, Model model) {
		return "/user/changePassword"; 
	}
	
	// 비밀번호 변경
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> changePassword(HttpSession session, @RequestBody Map<String, String> requestBody) {
		String userId = getUserIdFromSession(session);
		String oldPassword = requestBody.get("oldPw");
	    String newPassword = requestBody.get("newPw");
	    
	    String updateResult = service.checkCurrentPasswordAndChange(userId, oldPassword, newPassword);
	    Map<String, String> response = new HashMap<>();
	    if(updateResult.equals("success")) {
	    	session.invalidate(); // 비밀번호 변경 성공 시 로그아웃 처리
	    	response.put("status", "success");
	    	response.put("msg", "비밀번호가 변경되었습니다. 다시 로그인해 주세요.");
	    } else if(updateResult.equals("oldPasswordIncorrect")) {
	    	response.put("status", "oldPasswordIncorrect");
	    	response.put("msg", "현재 비밀번호가 일치하지 않습니다.");
	    } else if(updateResult.equals("isSameAsCurrentPassword")) {
	    	response.put("status", "isSameAsCurrentPassword");
	    	response.put("msg", "현재 비밀번호와 새 비밀번호가 동일합니다. 다른 비밀번호로 변경해주세요.");
	    } else {
	    	response.put("status", "error");
	    	response.put("msg", "알 수 없는 오류가 발생했습니다.");
	    }
	    return response;
	}
	
	// 프로필 변경 페이지
	@RequestMapping("/updateProfileForm")
	public String updateProfileForm(HttpSession session, Model model) {
		UserDto currentProfile = service.getCurrentProfile(getUserIdFromSession(session));
		model.addAttribute("uprofile_image", currentProfile.getUprofile_image());
		return "/user/updateProfile";
	}
	
	// 프로필 변경
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateProfile (
		@RequestParam(value = "nickname", required = false) String nickname,
		@RequestParam(value = "defaultImage", required = false) String defaultImage,
		@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
		@RequestParam(value = "isCustomImageSelected", defaultValue = "false") boolean isCustomImageSelected,
		HttpSession session) throws IOException {
		
		Map<String, String> map = new HashMap<>();
		String userId = getUserIdFromSession(session);
		map.put("userId", userId);
		
		Map<String, String> response = new HashMap<>();
		boolean updateResult = false;
		
		// 닉네임만 변경하는 경우
		if((defaultImage == null && profileImage == null) && nickname != null) {
			map.put("nickname", nickname);
			updateResult = service.updateNickname(map);
		} 
		// 프로필 이미지만 기본 이미지로 변경하는 경우
		else if(defaultImage != null && !isCustomImageSelected && nickname == null) {
			map.put("filename", defaultImage);
			updateResult = service.updateProfileImage(map);
		} 
		// 프로필 이미지만 업로드한 이미지로 변경하는 경우
		else if(profileImage != null && isCustomImageSelected && nickname == null) {
			String filename = saveProfileImage(profileImage, userId);
			map.put("filename", filename);
			updateResult = service.updateProfileImage(map);
		} 
		// 닉네임 변경 + 기본 이미지로 변경하는 경우
		else if(defaultImage != null && !isCustomImageSelected && nickname != null) {
			map.put("nickname", nickname);
			map.put("filename", defaultImage);
			updateResult = service.updateNicknameAndProfileImage(map);
		} 
		// 닉네임 변경 + 업로드한 이미지로 변경하는 경우
		else if(profileImage != null && isCustomImageSelected && nickname != null) {
			String filename = saveProfileImage(profileImage, userId);
			map.put("nickname", nickname);
			map.put("filename", filename);
			updateResult = service.updateNicknameAndProfileImage(map);
		} 
		
		// 응답 처리
		if(updateResult) {
			response.put("status", "success");
			response.put("msg", "프로필이 수정되었습니다.");
			if(nickname != null) { // 닉네임 변경을 한 경우
				session.setAttribute("userNickname", nickname);
			}
		} else {
			response.put("status", "fail");
			response.put("msg", "알 수 없는 오류가 발생했습니다.");
		}
		return response;
	}
	
	// 프로필 이미지 파일이름
	@RequestMapping("/getProfileFilename")
	public void getProfileFilename(@PathVariable String bName) {
		System.out.println("getProfileFilename 호출");
		System.out.println("bName: " + bName);
//		String filename = service.getProfileFilename(bName);
//		return filename;
	}
	
	// 프로필 이미지 파일 서빙
	@RequestMapping("/getProfileImage/{filename:.+}")
	@ResponseBody
	public Resource getProfileImage(@PathVariable String filename) {
		String uploadDir = "C:\\server_program\\profileImgTest\\";
		File file = new File(uploadDir, filename);
		
		if (file.exists()) {
			return new FileSystemResource(file); // file자체를 보내는 것은 X, HTTP 본문 응답으로 자동 변환해주는 API(FileSystemResource())를 이용해서 보내야 함
		} else {
			throw new RuntimeException("File not found");
		}
	}
	
	// 파일 저장
	private String saveProfileImage(MultipartFile profileImage, String userId) throws IOException {
		// 파일 저장 경로 설정
		String uploadDir = "C:\\server_program\\profileImgTest\\";
		String filename = userId + "_" + profileImage.getOriginalFilename();
		File fileToSave = new File(uploadDir, filename);
		
		// 파일 저장
		profileImage.transferTo(fileToSave);
		return filename;
	}
	
	// 1:1 채팅
	@RequestMapping("/chat/{receiver}")
	public String chatForm(@PathVariable String receiver, Model model) {
		model.addAttribute("unickName", service.getUserNickname(receiver));
		model.addAttribute("receiver", receiver);
		model.addAttribute("serverUrl", environmentService.getWebSocketServer());
		return "/user/chatPage";
	}
	
	// 사용자 인증 페이지
	@RequestMapping("/verify-user")
	public String showVerifyPage(@RequestParam String mode,
		                         @RequestParam(required = false) String userId,
		                         Model model) {
		if(mode.equals("password")) {
			model.addAttribute("verifyUserId", userId);
		}
		model.addAttribute("mode", mode);
		return "/user/verify-user";
	}
	
	// 사용자 이름, 이메일, (비밀번호 찾기인 경우) 아이디까지 일치하는지 확인
	// 아이디 찾기: userName + uemail만 확인
	// 비밀번호 찾기: userName + uemail + userId까지 확인
	@ResponseBody
	@RequestMapping("/checkUserInfoMatch")
	public boolean checkUserInfoMatch(@RequestParam String userName,
				                      @RequestParam String uemail,
				                      @RequestParam(required = false) String userId) {
		return service.checkUserInfoMatch(userName, uemail, userId);
	}
	
	// 아이디 찾기 - 인증 후 사용자정보 가져오기
	@RequestMapping("/findIdResultPage")
	public String findIdResultPage(@RequestParam Map<String, String> map, Model model) {
		String authMethod = map.get("authMethod");
		if(authMethod.equals("phone")) {
			System.out.println("휴대폰 인증");
			// 휴대폰 인증은 사용하지 않음
		} else if(authMethod.equals("email")) {
			String userName = map.get("userNameEmail");
			String domain = map.get("emailDomain");
			String uemail = "";
			uemail = domain.equals("direct")
				? map.get("directEmailDomain")
				: map.get("userEmail") + domain;
			
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userName", userName);
			hashMap.put("uemail", uemail);
			
			List<UserDto> findIdResultList = service.findUserIdByEmail(hashMap);
			model.addAttribute("findIdResultList", findIdResultList);
		}
		
		return "/user/findIdResultPage";
	}
	
	// 아이디 입력 받을 페이지
	@RequestMapping("/password-userid-input")
	public String showUseridInputPage() {
		return "/user/password-userid-input";
	}

	// 인증 단계: 사용자 아이디 존재여부 확인
	@ResponseBody
	@RequestMapping("/check-userid-match")
	public boolean checkUseridMatch(@RequestParam String userId) {
		return service.checkUserIdMatch(userId);
	}
	
	// 비밀번호 재설정 페이지
	@RequestMapping("/password-reset-page")
	public String showPasswordResetPage(@RequestParam String verifyUserId, Model model) {
		model.addAttribute("verifyUserId", verifyUserId);
		return "/user/password-reset";
	}
	
	// 비밀번호 재설정
	@RequestMapping(value = "/password-reset", method = RequestMethod.POST)
	public String updatePassword(@RequestParam String verifyUserId, 
								 @RequestParam String confirmPassword, 
								 RedirectAttributes rttr) {
		boolean result = isPasswordValid(confirmPassword);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(result) {
			// 사용자 비밀번호를 BCrypt 해시 알고리즘으로 단방향 암호화 처리
			String hashedPassword = passwordEncoder.encode(confirmPassword);
			boolean updateResult = service.resetPassword(verifyUserId, hashedPassword);
			
			if(updateResult) {
				System.out.println("비밀번호 변경 완료");
				rttr.addFlashAttribute("error", "비밀번호가 변경되었습니다.");
				return "redirect:/user/loginPage?reset=success"; 
			} else {
				System.out.println("비밀번호 변경 실패");
				rttr.addFlashAttribute("error", "비밀번호 변경에 실패했습니다. 다시 시도해주세요");
				return "redirect:/user/password-userid-input?reset=fail";
			}
		} else {
			rttr.addFlashAttribute("error", "잘못된 비밀번호 형식입니다.");
			System.out.println("비밀번호 유효성 검사 탈락");
			return "redirect:/user/password-userid-input?reset=fail";
		}
	}
	
	// 비밀번호 유효성 검사(서버에서 한번 더 검사)
	private boolean isPasswordValid(String password) {
		if(password == null || password.length() < 8 || password.length() > 16) return false;
		if(password.matches(".*\\s+.*")) return false; // 공백 체크
		if(!password.matches(".*[A-Z].*")) return false; // 대문자
		if(!password.matches(".*[a-z].*")) return false; // 소문자
		if(!password.matches(".*\\d.*")) return false; // 숫자
		if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\/,.<>\\/?].*")) return false; // 특수문자
		return true;
	}
	
	// 아이디 유효성 검사(서버에서 한번 더 검사)
	private boolean isIdValid(String userId) {
		if(userId == null || userId.length() < 4 || userId.length() > 12) return false;
		if(userId.matches(".*\\s+.*")) return false; // 공백 체크
		if(!userId.matches("^[a-z][a-z0-9_.]*$")) return false; // 소문자 시작 + 허용 문자만 사용
		return true;
	}
	
	// 아이디 중복 체크
	@ResponseBody
	@RequestMapping("/check-id-duplicate")
	public boolean checkIdDuplicate(@RequestParam String checkId) {
		return service.checkIdDuplicate(checkId);
	}
	
	// 닉네임 중복 검사
	@ResponseBody
	@RequestMapping("/check-nickname-duplicate")
	public boolean checkNicknameDuplicate(@RequestParam String checkNickanme) {
		return service.checkNicknameDuplicate(checkNickanme);
	}
}
