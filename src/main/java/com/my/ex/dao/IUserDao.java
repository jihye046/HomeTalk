package com.my.ex.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.ex.dto.BoardDto;
import com.my.ex.dto.UserDto;

public interface IUserDao {
	int join(UserDto dto);
	UserDto login(String userId, String userPw);
	String getUserPassword(String userId);
	String getUserNickname(String userId);
	List<BoardDto> getUserPosts(HashMap<String, String> map);
	List<BoardDto> getUserComments(String userId);
	List<BoardDto> getUserLikedPosts(String userId);
	int isOldPasswordCorrect(String userId, String oldPassword);
	String getCurrentPassword(String userId);
	boolean updatePassword(String userId, String newPassword);
	// 아이디 중복 검사
	int checkIdDuplicate(String checkId);
	// 닉네임 중복 검사
	int checkNicknameDuplicate(String checkNickanme);
	
	// 프로필 이미지, 닉네임
	UserDto getCurrentProfile(String userId);
	int updateNickname(Map<String, String> map); 
	int updateProfileImage(Map<String, String> map); 
	int updateNicknameAndProfileImage(Map<String, String> map);
	
	// 댓글 프로필 이미지 가져오기
	String getProfileFilename(String bName);
	
	// 사용자 이름, 이메일, (비밀번호 찾기인 경우) 아이디까지 일치하는지 확인
	// 아이디 찾기: userName + uemail만 확인
	// 비밀번호 찾기: userName + uemail + userId까지 확인
	int checkUserInfoMatch(HashMap<String, String> map);
	List<UserDto> findUserIdByEmail(HashMap<String, String> hashMap);
	
	// 비밀번호 찾기
	// 인증 단계: 아이디 존재여부 확인
	int checkUserIdMatch(String userId);
	int resetPassword(Map<String, String> map);
	
	// oauth 인증 관련
	int isSocialUserExists(HashMap<String, String> map);
}
