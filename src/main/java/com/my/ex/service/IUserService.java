package com.my.ex.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.ex.dto.BoardDto;
import com.my.ex.dto.UserDto;

public interface IUserService {
	boolean join(UserDto dto);
	boolean login(String userId, String userPw);
	String getUserPassword(String userId);
	String getUserNickname(String userId);
	List<BoardDto> getUserPosts(String userId, String searchGubun, String searchText, String sortType);
	List<BoardDto> getUserComments(String userId);
	List<BoardDto> getUserLikedPosts(String userId);
	String checkCurrentPasswordAndChange(String userId, String oldPassword, String newPassword);
	// 아이디 중복 검사
	boolean checkIdDuplicate(String checkId);
	// 닉네임 중복 검사
	boolean checkNicknameDuplicate(String checkNickanme);
	
	// 프로필 이미지, 닉네임
	UserDto getCurrentProfile(String userId);
	boolean updateNickname(Map<String, String> map); 
	boolean updateProfileImage(Map<String, String> map); 
	boolean updateNicknameAndProfileImage(Map<String, String> map);
	
	// 댓글 프로필 이미지 가져오기
	String getProfileFilename(String bName);
	
	// 사용자 이름, 이메일, (비밀번호 찾기인 경우) 아이디까지 일치하는지 확인
	// 아이디 찾기: userName + uemail만 확인
	// 비밀번호 찾기: userName + uemail + userId까지 확인
	boolean checkUserInfoMatch(String userName, String uemail, String userId);
	List<UserDto> findUserIdByEmail(HashMap<String, String> hashMap);
	
	// 비밀번호 찾기
	// 인증 단계: 아이디 존재여부 확인
	boolean checkUserIdMatch(String userId);
	boolean resetPassword(String userId, String password);

	// oauth 인증 관련
	boolean isSocialUserExists(String sns_id, String sns_type);
}
