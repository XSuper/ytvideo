package com.tugkgp.cn.control.cache;

import java.util.Date;

public class MemoryCache {

	private static MemoryCache memoryCache;
	private  java.util.Hashtable<String, Object> __cacheList;
	private MemoryCache() {
		__cacheList = new java.util.Hashtable<String, Object>();
	}
	public static MemoryCache getInstance(){
		if(memoryCache == null){
			memoryCache = new MemoryCache();
		}
		return memoryCache;
	}

	// 添加cache,不过期
	public synchronized void add(String key, Object value) {
		add(key, value, -1);
	}

	// 添加cache有过期时间
	public synchronized void add(String key, Object value, long timeOut) {
		add(key, value, timeOut, null);
	}

	// 添加cache有过期时间并且具有回调方法
	public synchronized void add(String key, Object value, long timeOut,
			ICacheMethod callback) {
		if (timeOut > 0) {
			timeOut += new Date().getTime();
		}
		MemoryCacheItem item = new MemoryCacheItem(key, value, timeOut, callback);
		__cacheList.put(key, item);
	}

	// 获取cache
	public synchronized Object get(String key) {
		Object obj = __cacheList.get(key);
		if (obj == null) {
			return null;
		}
		MemoryCacheItem item = (MemoryCacheItem) obj;
		boolean expired = cacheExpired(key);
		if (expired == true) // 已过期
		{
			if (item.getCallback() == null) {
				remove(key);
				return null;
			} else {
				ICacheMethod callback = item.getCallback();
				callback.execute(key);
				expired = cacheExpired(key);
				if (expired == true) {
					remove(key);
					return null;
				}
			}
		}
		return item.getValue();
	}

	// 移除cache
	public synchronized void remove(String key) {
		Object obj = __cacheList.get(key);
		if (obj != null) {
			obj = null;
		}
		__cacheList.remove(key);
	}

	// 清理所有cache对象
	public synchronized void clear() {

		for (String s : __cacheList.keySet()) {
			__cacheList.put(s, null);
		}
		__cacheList.clear();
	}

	// 判断是否过期
	private boolean cacheExpired(String key) {
		MemoryCacheItem item = (MemoryCacheItem) __cacheList.get(key);
		if (item == null) {
			return false;
		}
		long milisNow = new Date().getTime();
		long milisExpire = item.getTimeOut();
		if (milisExpire <= 0) { // 不过期
			return false;
		} else if (milisNow >= milisExpire) {
			return true;
		} else {
			return false;
		}
	}
}