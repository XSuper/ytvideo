package com.tugkgp.cn.control.cache;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

public class CacheManager {
	public  String TAG = this.getClass().getSimpleName();
	private MemoryCache memoryCache;
	private DataBaseCache baseCache;
	private static CacheManager cacheManager;
	
	private CacheManager(){
		memoryCache = MemoryCache.getInstance();
		baseCache = DataBaseCache.getInstance();
		
	}
	public static CacheManager getInstance(){
		if(cacheManager == null){
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}
	public void put(String key,Object value,long timeout,ICacheMethod callback,CachePolicy policy){
		switch (policy) {
		case PUT_MEMORY_DATEBASE:
			baseCache.add(key, value, timeout);
			break;

		case PUT_ONLY_MEMORY:
			break;
			
		default://默认都内存和数据库都缓存
			Log.e(TAG,"请使用put 规则，默认内存和数据库都做缓存");
			baseCache.add(key, value, timeout);
			break;
			
		}
		
		memoryCache.add(key, value, timeout, callback);
	}
	public void put(String key,Object value,long timeout,CachePolicy policy){
		put(key, value, timeout,null, policy);
	}
	public void put(String key,Object value,CachePolicy policy){
		put(key, value, -1,null, policy);
	}
	public void put(String key,Object value){
		put(key, value, -1,null, CachePolicy.PUT_MEMORY_DATEBASE);
	}
	public void put(String key,Object value,long timeout){
		put(key, value, timeout,null, CachePolicy.PUT_MEMORY_DATEBASE);
	}

	public Object get(String key,TypeToken token,CachePolicy policy){
		Object obj = null;
		switch (policy) {
		case PUT_MEMORY_DATEBASE:
		case PUT_ONLY_MEMORY:
			Log.e(TAG, "请使用get规则，默认GET_MEMORY_DATEBASE方式");
		case GET_MEMORY_DATEBASE:
			obj = memoryCache.get(key);
			if(obj==null){
			DataBaseCacheItem item = baseCache.getItem(key, token);
				if(item!= null){
					obj = baseCache.get(key, token);
					memoryCache.add(key, obj, item.getTimeOut());
				}
			}
			break;
		case GET_ONLY_MEMORY:
			obj = memoryCache.get(key);
			break;
		
		}
		return obj;
	}
	
	public Object getFromMemoryCache(String key){
		return memoryCache.get(key);
	}
	public Object get(String key,TypeToken token){
		return get(key,token,CachePolicy.GET_MEMORY_DATEBASE);
	}
}
