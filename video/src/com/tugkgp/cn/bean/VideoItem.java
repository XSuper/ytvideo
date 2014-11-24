package com.tugkgp.cn.bean;

import com.lidroid.xutils.db.annotation.Id;

public class VideoItem {
	@Id
	private String itemid;
	private String totaltime;
	private String picurl;
	private String title;
	private String playurl;
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlayurl() {
		return playurl;
	}
	public void setPlayurl(String playurl) {
		this.playurl = playurl;
	}
	
	

}
