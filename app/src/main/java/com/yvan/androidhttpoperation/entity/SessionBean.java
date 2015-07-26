package com.yvan.androidhttpoperation.entity;

public class SessionBean {
	private String UID;
	private String title;
	private String content;
	// private Date date;
	private int praiseCount;
	private int commentCount;

	// private String headPortrait;
	// private String[] images;
	public SessionBean() {
	}

	public SessionBean(String uID, String title, String content,
			int praiseCount, int commentCount) {
		UID = uID;
		this.title = title;
		this.content = content;
		this.praiseCount = praiseCount;
		this.commentCount = commentCount;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// public Date getDate() {
	// return date;
	// }
	//
	// public void setDate(Date date) {
	// this.date = date;
	// }

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	// public String getHeadPortrait() {
	// return headPortrait;
	// }
	//
	// public void setHeadPortrait(String headPortrait) {
	// this.headPortrait = headPortrait;
	// }
	//
	// public String[] getImages() {
	// return images;
	// }
	//
	// public void setImages(String[] images) {
	// this.images = images;
	// }

}
