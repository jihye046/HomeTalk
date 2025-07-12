package com.my.ex;

import org.springframework.beans.factory.annotation.Value;


public class EnvironmentService {

	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	@Value("${image.devInitRequest.url}")
	private String devInitRequestUrl;
	
	@Value("${image.prodInitRequest.url}")
	private String prodInitRequestUrl;
	
	@Value("${image.devUpload.path}")
	private String devUploadPath;
	
	@Value("${image.prodUpload.path}")
	private String prodUploadPath;
	
	@Value("${image.devAccess.url}")
	private String devAccessUrl;
	
	@Value("${image.prodAccess.url}")
	private String prodAccessUrl;
	
	@Value("${ws.dev.webSocket}")
	private String devWebSocket;
	
	@Value("${ws.prod.webSocket}")
	private String prodWebSocket;
	
	public EnvironmentService() {}

	public EnvironmentService(String activeProfile, String devInitRequestUrl, String prodInitRequestUrl,
			String devUploadPath, String prodUploadPath, String devAccessUrl, String prodAccessUrl, String devWebSocket,
			String prodWebSocket) {
		this.activeProfile = activeProfile;
		this.devInitRequestUrl = devInitRequestUrl;
		this.prodInitRequestUrl = prodInitRequestUrl;
		this.devUploadPath = devUploadPath;
		this.prodUploadPath = prodUploadPath;
		this.devAccessUrl = devAccessUrl;
		this.prodAccessUrl = prodAccessUrl;
		this.devWebSocket = devWebSocket;
		this.prodWebSocket = prodWebSocket;
	}

	public String getActiveProfile() {
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}

	public String getDevInitRequestUrl() {
		return devInitRequestUrl;
	}

	public void setDevInitRequestUrl(String devInitRequestUrl) {
		this.devInitRequestUrl = devInitRequestUrl;
	}

	public String getProdInitRequestUrl() {
		return prodInitRequestUrl;
	}

	public void setProdInitRequestUrl(String prodInitRequestUrl) {
		this.prodInitRequestUrl = prodInitRequestUrl;
	}

	public String getDevUploadPath() {
		return devUploadPath;
	}

	public void setDevUploadPath(String devUploadPath) {
		this.devUploadPath = devUploadPath;
	}

	public String getProdUploadPath() {
		return prodUploadPath;
	}

	public void setProdUploadPath(String prodUploadPath) {
		this.prodUploadPath = prodUploadPath;
	}

	public String getDevAccessUrl() {
		return devAccessUrl;
	}

	public void setDevAccessUrl(String devAccessUrl) {
		this.devAccessUrl = devAccessUrl;
	}

	public String getProdAccessUrl() {
		return prodAccessUrl;
	}

	public void setProdAccessUrl(String prodAccessUrl) {
		this.prodAccessUrl = prodAccessUrl;
	}

	public String getDevWebSocket() {
		return devWebSocket;
	}

	public void setDevWebSocket(String devWebSocket) {
		this.devWebSocket = devWebSocket;
	}

	public String getProdWebSocket() {
		return prodWebSocket;
	}

	public void setProdWebSocket(String prodWebSocket) {
		this.prodWebSocket = prodWebSocket;
	}

	// 파일 업로드 요청 url
	public String getInitRequest() {
		if("prod".equals(activeProfile)) {
			return prodInitRequestUrl;
		}
		return devInitRequestUrl;
	}
	
	// 저장소
	public String getUploadPath() {
		if("prod".equals(activeProfile)) {
			return prodUploadPath;
		}
		return devUploadPath;
	}
	
	// url
	public String getAccessPath() {
		if("prod".equals(activeProfile)) {
			return prodAccessUrl;
		}
		return devAccessUrl;
	}
	
	// webSocket Server
	public String getWebSocketServer() {
		if("prod".equals(activeProfile)) {
			return  prodWebSocket;
		}
		return devWebSocket;
	}

}
