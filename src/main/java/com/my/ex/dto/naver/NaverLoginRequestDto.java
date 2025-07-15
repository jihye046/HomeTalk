package com.my.ex.dto.naver;

import org.springframework.beans.factory.annotation.Value;

public class NaverLoginRequestDto {
	
	@Value("${naver.baseurl}")
	private String baseurl;
	
	@Value("${naver.response_type}")
	private String response_type;
	
	@Value("${naver.client_id}")
	private String client_id;
	
	@Value("${naver.client_secret}")
	private String client_secret;
	
	@Value("${naver.state}")
	private String state;

	@Value("${naver.redirect_uri}")
	private String redirect_uri;
	
	@Value("${naver.redirect_prod_uri}")
	private String redirect_prod_uri;

	public NaverLoginRequestDto() {}

	public NaverLoginRequestDto(String baseurl, String response_type, String client_id, String client_secret,
			String state, String redirect_uri, String redirect_prod_uri) {
		this.baseurl = baseurl;
		this.response_type = response_type;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.state = state;
		this.redirect_uri = redirect_uri;
		this.redirect_prod_uri = redirect_prod_uri;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getRedirect_prod_uri() {
		return redirect_prod_uri;
	}

	public void setRedirect_prod_uri(String redirect_prod_uri) {
		this.redirect_prod_uri = redirect_prod_uri;
	}

	@Override
	public String toString() {
		return "NaverLoginRequestDto [baseurl=" + baseurl + ", response_type=" + response_type + ", client_id="
				+ client_id + ", client_secret=" + client_secret + ", state=" + state + ", redirect_uri=" + redirect_uri
				+ ", redirect_prod_uri=" + redirect_prod_uri + "]";
	}

}
