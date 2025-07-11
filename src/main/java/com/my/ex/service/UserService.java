package com.my.ex.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.ex.dao.UserDao;
import com.my.ex.dto.BoardDto;
import com.my.ex.dto.UserDto;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserDao dao;
	
	// 회원가입
	@Override
	public boolean join(UserDto dto) {
		if(dto.getUserPw() == null || dto.getUserPw() == ""){
			dto.setUserPw(null); // oauth 인증을 통한 회원가입 시 비밀번호 값이 없음. 확실하게 null로 지정
		}
		
		return dao.join(dto) > 0;
	}

	// 로그인
	@Override
	public boolean login(String userId, String userPw) {
		UserDto dto = dao.login(userId, userPw);
		return (dto != null) ? true	: false;
	}

	@Override
	public String getUserNickname(String userId) {
		return dao.getUserNickname(userId);
	}

	@Override
	public List<BoardDto> getUserPosts(String userId, String searchGubun, String searchText, String sortType) {
		HashMap<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("searchGubun",searchGubun);
		map.put("searchText", searchText);
		map.put("sortType", sortType);
		
		return dao.getUserPosts(map);
	}

	@Override
	public List<BoardDto> getUserComments(String userId) {
		return dao.getUserComments(userId);
	}

	@Override
	public List<BoardDto> getUserLikedPosts(String userId) {
		return dao.getUserLikedPosts(userId);
	}

	// 비밀번호 변경
	@Override
	public String checkCurrentPasswordAndChange(String userId, String oldPassword, String newPassword) {
		if (dao.isOldPasswordCorrect(userId, oldPassword) != 1) {
	        return "oldPasswordIncorrect"; // 현재 비밀번호가 틀린 경우
	    } else if (dao.getCurrentPassword(userId).equals(newPassword)) {
	    	return "isSameAsCurrentPassword"; // 현재 비밀번호와 새 비밀번호가 동일한 경우
	    }
	    return dao.updatePassword(userId, newPassword) ? "success" : "fail";
	}
	
	// 현재 프로필 정보 조회
	@Override
	public UserDto getCurrentProfile(String userId) {
		return dao.getCurrentProfile(userId);
	}
	
	// 닉네임만 변경하는 경우
	@Override
	public boolean updateNickname(Map<String, String> map) {
		return dao.updateNickname(map) > 0;
	}
	
	// 프로필 이미지만 변경하는 경우
	@Override
	public boolean updateProfileImage(Map<String, String> map) {
		return dao.updateProfileImage(map) > 0;
	}

	// 닉네임 + 프로필 이미지 변경하는 경우
	@Override
	public boolean updateNicknameAndProfileImage(Map<String, String> map) {
		return dao.updateNicknameAndProfileImage(map) > 0;
	}

	// 댓글 프로필 이미지 가져오기
	@Override
	public String getProfileFilename(String bName) {
		return dao.getProfileFilename(bName);
	}

	// 사용자 이름, 이메일, (비밀번호 찾기인 경우) 아이디까지 일치하는지 확인
	// 아이디 찾기: userName + uemail만 확인
	// 비밀번호 찾기: userName + uemail + userId까지 확인
	@Override
	public boolean checkUserInfoMatch(String userName, String uemail, String userId) {
		HashMap<String, String> map = new HashMap<>();
		map.put("userName", userName);
		map.put("uemail", uemail);
		map.put("userId", userId);
		int selectCount = dao.checkUserInfoMatch(map);
		
		return selectCount > 0;
	}

	// 아이디 찾기 - 인증 후 사용자정보 가져오기
	@Override
	public List<UserDto> findUserIdByEmail(HashMap<String, String> hashMap) {
		return dao.findUserIdByEmail(hashMap);
	}
	
	// 인증 단계: 사용자 아이디 존재여부 확인
	@Override
	public boolean checkUserIdMatch(String userId) {
		int selectCount = dao.checkUserIdMatch(userId);
		return selectCount > 0;
	}

	@Override
	public boolean resetPassword(String userId, String password) {
		HashMap<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		
		return dao.resetPassword(map) > 0;
	}

	@Override
	public String getUserPassword(String userId) {
		return dao.getUserPassword(userId);
	}
	
	// 아이디 중복 검사
	@Override
	public boolean checkIdDuplicate(String checkId) {
		return dao.checkIdDuplicate(checkId) > 0;
	}
	
	// 닉네임 중복 검사
	@Override
	public boolean checkNicknameDuplicate(String checkNickanme) {
		return dao.checkNicknameDuplicate(checkNickanme) > 0;
	}

	// 소셜 아이디 존재 여부 확인
	@Override
	public boolean isSocialUserExists(String sns_id, String sns_type) {
		HashMap<String, String> map = new HashMap<>();
		map.put("userId", sns_id);
		map.put("user_type", sns_type);
		return dao.isSocialUserExists(map) > 0;
	}

}
