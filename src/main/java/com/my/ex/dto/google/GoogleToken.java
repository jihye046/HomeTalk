package com.my.ex.dto.google;

public class GoogleToken {
	private String access_token;
	private String refresh_token;
	private String token_type;
	private int expires_in;
	private String scope;
	private String id_token;
	private String error;
	private String error_description;
	
	public GoogleToken() {}

	public GoogleToken(String access_token, String refresh_token, String token_type, int expires_in, String scope,
			String id_token, String error, String error_description) {
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.scope = scope;
		this.id_token = id_token;
		this.error = error;
		this.error_description = error_description;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	@Override
	public String toString() {
		return "GoogleToken [access_token=" + access_token + ", refresh_token=" + refresh_token + ", token_type="
				+ token_type + ", expires_in=" + expires_in + ", scope=" + scope + ", id_token=" + id_token + ", error="
				+ error + ", error_description=" + error_description + "]";
	}

}
