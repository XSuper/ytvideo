package com.tugkgp.cn.control.cache;

import com.lidroid.xutils.db.annotation.Id;


public class DataBaseCacheItem {

	@Id
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public DataBaseCacheItem(){
		
	}
	private String key;
	private String value;
	private long timeOut;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	public DataBaseCacheItem(String key, String value, long timeOut) {
		super();
		this.key = key;
		this.value = value;
		this.timeOut = timeOut;
	}
	
	
	

}
