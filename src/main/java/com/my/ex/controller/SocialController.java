package com.my.ex.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.ex.dto.SocialDto;
import com.my.ex.dto.UserDto;
import com.my.ex.dto.google.GoogleCallbackDto;
import com.my.ex.dto.google.GoogleProfileApi;
import com.my.ex.dto.google.GoogleToken;
import com.my.ex.dto.naver.NaverCallbackDto;
import com.my.ex.dto.naver.NaverProfileApi;
import com.my.ex.dto.naver.NaverToken;
import com.my.ex.service.SocialService;
import com.my.ex.service.UserService;

@Controller
@RequestMapping("/social")
public class SocialController {
	
	@Autowired
	private NaverCallbackDto naverCallbackDto;
	
	@Autowired
	private GoogleCallbackDto googleCallbackDto;
	
	@Autowired
	private SocialService service;
	
	@Autowired
	private UserService userService;
	
	GoogleToken token;
	
	// 네이버 로그인 연동 URL 생성
	@RequestMapping("/naverLogin")
	public String naverLogin(HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
		return "redirect:" + service.getNaverAuthorizeUrl("authorize");
	}
	
	// 네이버 로그인 연동 결과 callback
	@RequestMapping("/naverCallback")
	public String naverCallback(HttpServletRequest request) {
		naverCallbackDto.setCallbackCode(request.getParameter("code"));
		naverCallbackDto.setCallbackState(request.getParameter("state"));
		naverCallbackDto.setCallbackError(request.getParameter("error"));
		naverCallbackDto.setCallbackError_Description(request.getParameter("error_description"));
		return "redirect:naverGetUserInfo";
	}
	
	// callback 성공 시 받은 code를 이용하여 accessToken을 발급 받고 이를 이용하여 사용자 정보 얻기 
	@RequestMapping("naverGetUserInfo")
	public String naverGetUserInfo(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) throws URISyntaxException, Exception {
		if(naverCallbackDto.getCallbackError() == null || naverCallbackDto.getCallbackError() == "") {
			ObjectMapper mapper = new ObjectMapper();
			// 응답받은 json 데이터를 해당 클래스 객체로 변환, JSON 데이터의 '키'와 클래스의 멤버 변수 이름이 일치하는 경우 자동으로 매핑
			// code 주고 Accesstoken 받기
			String responseToken = service.getNaverTokenUrl("token", "authorization_code", naverCallbackDto);
			NaverToken token = mapper.readValue(responseToken, NaverToken.class); // Accesstoken 매핑
			
			// Accesstoken 주고 userInfo 받기
			String responseUser = service.getNaverUserByToken(token);
			NaverProfileApi naverUser = mapper.readValue(responseUser, NaverProfileApi.class); // userInfo 매핑
			
			boolean result = userService.isSocialUserExists(naverUser.getResponse().getId(), "NAVER");
			if(result) {
				// 소셜 회원가입이 되어있다면
				UserDto dto = userService.getCurrentProfile(naverUser.getResponse().getId()); // 정보 가져오기
				return targetLocation(session, dto);
			} else {
				UserDto dto = new UserDto();
				
				dto.setUsername(naverUser.getResponse().getName());
				dto.setUser_type("NAVER");
				dto.setUemail(naverUser.getResponse().getEmail());;
				dto.setUserId(naverUser.getResponse().getId());
				dto.setUnickName(naverUser.getResponse().getNickname());
				dto.setUmobile(naverUser.getResponse().getMobile());
				
				model.addAttribute("socialDto", dto);
				return "/user/socialJoinPage";
			}
			
		} else {
			System.out.println(naverCallbackDto.getCallbackError_Description());
			return "/user/loginPage";
		}
	}
	
	// 구글 로그인 연동 URL 생성
	@RequestMapping("/googleLogin")
	public String googleLogin(HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
		return "redirect:" + service.getGoogleAuthorizeUrl();
	}
	
	// 구글 로그인 연동 결과 callback
	@RequestMapping("/googleCallback")
	public String googleCallback(HttpServletRequest request) {
		googleCallbackDto.setCallbackCode(request.getParameter("code"));
		googleCallbackDto.setCallbackState(request.getParameter("state"));
		googleCallbackDto.setCallbackError(request.getParameter("error"));
		googleCallbackDto.setCallbackError_Description(request.getParameter("error_description"));
		return "redirect:getGoogleToken";
	}
	
	// callback 성공 시 받은 code를 이용하여 accessToken을 발급 받고 이를 이용하여 사용자 정보 얻기 
	@RequestMapping("/getGoogleToken")
	public String getGoogleToken(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) throws URISyntaxException, Exception {
		if(googleCallbackDto.getCallbackError() == null || googleCallbackDto.getCallbackError() == "") {
			ObjectMapper mapper = new ObjectMapper();
			// code 주고 Accesstoken 받기
			String responseToken = service.getGoogleTokenUrl("token", "authorization_code", googleCallbackDto);
			GoogleToken token = mapper.readValue(responseToken, GoogleToken.class);
			
			// Accesstoken 주고 userInfo 받기
			String responseUser = service.getGoogleUserByToken("tokeninfo", token);
			GoogleProfileApi googleUser = mapper.readValue(responseUser, GoogleProfileApi.class);
			boolean result = userService.isSocialUserExists(googleUser.getSub(), "GOOGLE");
			
			if(result) {
				// 소셜 회원가입이 되어있다면
				UserDto dto = userService.getCurrentProfile(googleUser.getSub()); // 정보 가져오기
				return targetLocation(session, dto);
			} else {
				// 소셜 회원가입이 되어있지 않다면 구글로부터 받은 정보를 담아 회원가입 페이지로 이동
				UserDto dto = new UserDto();
				
				String name = googleUser.getName(); 
				String family_name = googleUser.getFamily_name();
				dto.setUsername((name == null ? "" : name) + (family_name == null ? "" : family_name)); // 이름
				dto.setUser_type("GOOGLE"); // 타입
				dto.setUemail(googleUser.getEmail());
				dto.setUserId(googleUser.getSub());
				
				model.addAttribute("socialDto", dto);
				return "/user/socialJoinPage";
			}
			
		} else {
			System.out.println(googleCallbackDto.getCallbackError_Description());
			return "/user/loginPage";
		}
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String checkSocialIdExist(HttpSession session, UserDto dto) {
		userService.join(dto);
		
		return targetLocation(session, dto);
	}
	
	public String targetLocation(HttpSession session, UserDto dto) {
		session.setAttribute("userId", dto.getUserId());
		session.setAttribute("userNickname", dto.getUnickName());
		String targetLocation = (String)session.getAttribute("targetLocation");
		String redirectLocation = (targetLocation != null) ? "redirect:" + targetLocation : "redirect:/board/paging";
		return redirectLocation;
	}
	
}
