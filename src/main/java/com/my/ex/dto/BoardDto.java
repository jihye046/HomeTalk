package com.my.ex.dto;

import java.sql.Date;
import java.util.List;

public class BoardDto {
	private int bId;
	private String bName;
	private String bTitle;
	private String bContent;
	private Date bDate;
	private int bHit;
	private int bGroup;
	private int bStep;
	private int bIndent;
	private String bLike;
	private int bBookmark;
	private int commentCount;
	private String searchGubun;
	private String searchText;
	private String sortType;
	private boolean recommended;
	private String isDeleted;
	private String bAddress;
	private String tags;
	private String unickName;
	
	public BoardDto() {}

	public BoardDto(int bId, String bName, String bTitle, String bContent, Date bDate, int bHit, int bGroup, int bStep,
			int bIndent, String bLike, int bBookmark, int commentCount, String searchGubun, String searchText,
			String sortType, boolean recommended, String isDeleted, String bAddress, String tags, String unickName) {
		this.bId = bId;
		this.bName = bName;
		this.bTitle = bTitle;
		this.bContent = bContent;
		this.bDate = bDate;
		this.bHit = bHit;
		this.bGroup = bGroup;
		this.bStep = bStep;
		this.bIndent = bIndent;
		this.bLike = bLike;
		this.bBookmark = bBookmark;
		this.commentCount = commentCount;
		this.searchGubun = searchGubun;
		this.searchText = searchText;
		this.sortType = sortType;
		this.recommended = recommended;
		this.isDeleted = isDeleted;
		this.bAddress = bAddress;
		this.tags = tags;
		this.unickName = unickName;
	}

	public int getbId() {
		return bId;
	}

	public void setbId(int bId) {
		this.bId = bId;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getbTitle() {
		return bTitle;
	}

	public void setbTitle(String bTitle) {
		this.bTitle = bTitle;
	}

	public String getbContent() {
		return bContent;
	}

	public void setbContent(String bContent) {
		this.bContent = bContent;
	}

	public Date getbDate() {
		return bDate;
	}

	public void setbDate(Date bDate) {
		this.bDate = bDate;
	}

	public int getbHit() {
		return bHit;
	}

	public void setbHit(int bHit) {
		this.bHit = bHit;
	}

	public int getbGroup() {
		return bGroup;
	}

	public void setbGroup(int bGroup) {
		this.bGroup = bGroup;
	}

	public int getbStep() {
		return bStep;
	}

	public void setbStep(int bStep) {
		this.bStep = bStep;
	}

	public int getbIndent() {
		return bIndent;
	}

	public void setbIndent(int bIndent) {
		this.bIndent = bIndent;
	}

	public String getbLike() {
		return bLike;
	}

	public void setbLike(String bLike) {
		this.bLike = bLike;
	}

	public int getbBookmark() {
		return bBookmark;
	}

	public void setbBookmark(int bBookmark) {
		this.bBookmark = bBookmark;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getSearchGubun() {
		return searchGubun;
	}

	public void setSearchGubun(String searchGubun) {
		this.searchGubun = searchGubun;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getbAddress() {
		return bAddress;
	}

	public void setbAddress(String bAddress) {
		this.bAddress = bAddress;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getUnickName() {
		return unickName;
	}

	public void setUnickName(String unickName) {
		this.unickName = unickName;
	}

	@Override
	public String toString() {
		return "BoardDto [bId=" + bId + ", bName=" + bName + ", bTitle=" + bTitle + ", bContent=" + bContent
				+ ", bDate=" + bDate + ", bHit=" + bHit + ", bGroup=" + bGroup + ", bStep=" + bStep + ", bIndent="
				+ bIndent + ", bLike=" + bLike + ", bBookmark=" + bBookmark + ", commentCount=" + commentCount
				+ ", searchGubun=" + searchGubun + ", searchText=" + searchText + ", sortType=" + sortType
				+ ", recommended=" + recommended + ", isDeleted=" + isDeleted + ", bAddress=" + bAddress + ", tags="
				+ tags + ", unickName=" + unickName + "]";
	}

}
