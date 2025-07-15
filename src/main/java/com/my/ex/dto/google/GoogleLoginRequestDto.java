package com.my.ex.dto.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.my.ex.EnvironmentService;

import lombok.Builder;

@Builder
public class GoogleLoginRequestDto {
	
	@Value("${google.baseurl}")
	private String baseurl;
	
	@Value("${google.response_type}")
	private String response_type;
	
	@Value("${google.client_id}")
	private String client_id;
	
	@Value("${google.client_secret}")
	private String client_secret;
	
	@Value("${google.state}")
	private String state;
	
	@Value("${google.redirect_uri}")
	private String redirect_uri;
	
	@Value("${google.scope}")
	private String scope;
	
	@Value("${google.access_type}")
	private String access_type;
	
	@Value("${google.oauthBaseUri}")
	private String oauthBaseUri;
	
	@Value("${google.redirect_prod_uri}")
	private String redirect_prod_uri;

	public GoogleLoginRequestDto() {}

	public GoogleLoginRequestDto(String baseurl, String response_type, String client_id, String client_secret,
			String state, String redirect_uri, String scope, String access_type, String oauthBaseUri,
			String redirect_prod_uri) {
		this.baseurl = baseurl;
		this.response_type = response_type;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.state = state;
		this.redirect_uri = redirect_uri;
		this.scope = scope;
		this.access_type = access_type;
		this.oauthBaseUri = oauthBaseUri;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccess_type() {
		return access_type;
	}

	public void setAccess_type(String access_type) {
		this.access_type = access_type;
	}

	public String getOauthBaseUri() {
		return oauthBaseUri;
	}

	public void setOauthBaseUri(String oauthBaseUri) {
		this.oauthBaseUri = oauthBaseUri;
	}

	public String getRedirect_prod_uri() {
		return redirect_prod_uri;
	}

	public void setRedirect_prod_uri(String redirect_prod_uri) {
		this.redirect_prod_uri = redirect_prod_uri;
	}

	@Override
	public String toString() {
		return "GoogleLoginRequestDto [baseurl=" + baseurl + ", response_type=" + response_type + ", client_id="
				+ client_id + ", client_secret=" + client_secret + ", state=" + state + ", redirect_uri=" + redirect_uri
				+ ", scope=" + scope + ", access_type=" + access_type + ", oauthBaseUri=" + oauthBaseUri
				+ ", redirect_prod_uri=" + redirect_prod_uri + "]";
	}
	
}
