package com.my.ex.dto;

import java.sql.Date;

public class UserDto {
	private String username; 
	private String umobile; 
	private String userId; 
	private String userPw;
	private String unickName;
	private String uemail; 
	private int upoint;
	private Date createDate;
	private Date modifyDate;
	private String uprofileImage;
	private String userType;
	
	public UserDto() {}

	public UserDto(String username, String umobile, String userId, String userPw, String unickName, String uemail,
			int upoint, Date createDate, Date modifyDate, String uprofileImage, String userType) {
		this.username = username;
		this.umobile = umobile;
		this.userId = userId;
		this.userPw = userPw;
		this.unickName = unickName;
		this.uemail = uemail;
		this.upoint = upoint;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.uprofileImage = uprofileImage;
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUmobile() {
		return umobile;
	}

	public void setUmobile(String umobile) {
		this.umobile = umobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUnickName() {
		return unickName;
	}

	public void setUnickName(String unickName) {
		this.unickName = unickName;
	}

	public String getUemail() {
		return uemail;
	}

	public void setUemail(String uemail) {
		this.uemail = uemail;
	}

	public int getUpoint() {
		return upoint;
	}

	public void setUpoint(int upoint) {
		this.upoint = upoint;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getUprofileImage() {
		return uprofileImage;
	}

	public void setUprofileImage(String uprofileImage) {
		this.uprofileImage = uprofileImage;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", umobile=" + umobile + ", userId=" + userId + ", userPw=" + userPw
				+ ", unickName=" + unickName + ", uemail=" + uemail + ", upoint=" + upoint + ", createDate="
				+ createDate + ", modifyDate=" + modifyDate + ", uprofileImage=" + uprofileImage + ", userType="
				+ userType + "]";
	}
	
}
