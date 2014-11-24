package com.tugkgp.cn.control.cache;

import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.tugkgp.cn.base.MApplication;
import com.tugkgp.cn.util.MD5;

/**
 * 数据库缓存
 * 
 */
public class DataBaseCache {

	private DbUtils db;
	private static DataBaseCache baseCache;

	private DataBaseCache() {
		db = DbUtils.create(MApplication.getApplication(), "cache");
	}

	public static DataBaseCache getInstance() {
		if (baseCache == null) {
			baseCache = new DataBaseCache();
		}
		return baseCache;
	}

	// 添加cache,不过期
	public synchronized void add(String key, Object value) {
		add(key, value, -1);
	}

	// 添加cache有过期时间
	public synchronized void add(String key, Object value, long timeOut) {
		if (timeOut > 0) {
			timeOut += new Date().getTime();
		}
		DataBaseCacheItem item = null;
		try {
			item = db.findFirst(Selector.from(DataBaseCacheItem.class).where(
					"key", "=", key));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String var = gson.toJson(value);
		if (item == null) {
			item = new DataBaseCacheItem(key, var, timeOut);
			try {
				db.save(item);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			item.setTimeOut(timeOut);
			item.setValue(var);
			try {
				db.update(item, "value", "timeOut");
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (item != null) {
			try {
				db.save(item);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 获取cache
	public synchronized <T> T get(String key, Class<T> T) {
		DataBaseCacheItem item = null;
		try {
			item = db.findFirst(Selector.from(DataBaseCacheItem.class).where(
					"key", "=", key));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (item == null) {
			return null;
		}
		String obj = item.getValue();
		if (obj == null) {
			return null;
		}

		boolean expired = cacheExpired(item);
		if (expired == true) // 已过期
		{
			remove(key);
			return null;
		}
		Gson g = new Gson();
		return g.fromJson(obj, T);
	}

	public synchronized DataBaseCacheItem getItem(String key, TypeToken token) {
		DataBaseCacheItem item = null;
		try {
			item = db.findFirst(Selector.from(DataBaseCacheItem.class).where(
					"key", "=", key));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (item == null) {
			return null;
		}
		boolean expired = cacheExpired(item);
		if (expired == true) // 已过期
		{
			remove(key);
			return null;
		}
		return item;
	}
	public synchronized Object get(String key, TypeToken token) {
		DataBaseCacheItem item = getItem(key, token);
		if (item == null) {
			return null;
		}
		String obj = item.getValue();
		if (obj == null) {
			return null;
		}
		Gson g = new Gson();
		return g.fromJson(obj, token.getType());
	}

	// 移除cache
	public synchronized void remove(String key) {
		try {
			db.delete(MemoryCacheItem.class, WhereBuilder.b("key", "=", key));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 清理所有cache对象
	public synchronized void clear() {

		try {
			db.deleteAll(MemoryCacheItem.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 判断是否过期
	private static boolean cacheExpired(DataBaseCacheItem item) {
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

	private static String buildKey(String url, Map<String, Object> params) {
		if (params != null) {
			url += params.toString();
		}
		try {
			return MD5.encryptMD5(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
