package com.my.ex.dto;

import java.sql.Date;

public class SocialDto {
	private String snsId;
	private String snsNickName;
	private String upoint;
	private String snsEmail;
	private String snsName;
	private String snsMobile;
	private String snsType;
	private String snsProfile;
	private Date snsConnectDate;

	public SocialDto() {}

	public SocialDto(String snsId, String snsNickName, String upoint, String snsEmail, String snsName, String snsMobile,
			String snsType, String snsProfile, Date snsConnectDate) {
		this.snsId = snsId;
		this.snsNickName = snsNickName;
		this.upoint = upoint;
		this.snsEmail = snsEmail;
		this.snsName = snsName;
		this.snsMobile = snsMobile;
		this.snsType = snsType;
		this.snsProfile = snsProfile;
		this.snsConnectDate = snsConnectDate;
	}

	public String getSnsId() {
		return snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}

	public String getSnsNickName() {
		return snsNickName;
	}

	public void setSnsNickName(String snsNickName) {
		this.snsNickName = snsNickName;
	}

	public String getUpoint() {
		return upoint;
	}

	public void setUpoint(String upoint) {
		this.upoint = upoint;
	}

	public String getSnsEmail() {
		return snsEmail;
	}

	public void setSnsEmail(String snsEmail) {
		this.snsEmail = snsEmail;
	}

	public String getSnsName() {
		return snsName;
	}

	public void setSnsName(String snsName) {
		this.snsName = snsName;
	}

	public String getSnsMobile() {
		return snsMobile;
	}

	public void setSnsMobile(String snsMobile) {
		this.snsMobile = snsMobile;
	}

	public String getSnsType() {
		return snsType;
	}

	public void setSnsType(String snsType) {
		this.snsType = snsType;
	}

	public String getSnsProfile() {
		return snsProfile;
	}

	public void setSnsProfile(String snsProfile) {
		this.snsProfile = snsProfile;
	}

	public Date getSnsConnectDate() {
		return snsConnectDate;
	}

	public void setSnsConnectDate(Date snsConnectDate) {
		this.snsConnectDate = snsConnectDate;
	}

	@Override
	public String toString() {
		return "SocialDto [snsId=" + snsId + ", snsNickName=" + snsNickName + ", upoint=" + upoint + ", snsEmail="
				+ snsEmail + ", snsName=" + snsName + ", snsMobile=" + snsMobile + ", snsType=" + snsType
				+ ", snsProfile=" + snsProfile + ", snsConnectDate=" + snsConnectDate + "]";
	}
	
}
