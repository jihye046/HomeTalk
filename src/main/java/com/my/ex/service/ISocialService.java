package com.my.ex.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.my.ex.dto.SocialDto;
import com.my.ex.dto.google.GoogleCallbackDto;
import com.my.ex.dto.google.GoogleToken;
import com.my.ex.dto.naver.NaverCallbackDto;
import com.my.ex.dto.naver.NaverToken;

public interface ISocialService {
	String getNaverAuthorizeUrl(String type) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
	String getNaverTokenUrl(String type, String grant_type, NaverCallbackDto naverCallbackDto) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
	String getNaverUserByToken(NaverToken token);
	
	String getGoogleAuthorizeUrl() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
	String getGoogleTokenUrl(String type, String grant_type, GoogleCallbackDto googleCallbackDto) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
	String getGoogleUserByToken(String type, GoogleToken token);
	String readResponse(HttpURLConnection connection) throws IOException;
}
