package com.tugkgp.cn.util;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.google.gson.Gson;

public class PropertiesUtil {

	
	public String price; //价格
	public int pricetype; //价格类型
	public int op;
	public String chid;
	
	
	
	private static PropertiesUtil pUtil;
	public static PropertiesUtil getInstance(Context context){
		if(pUtil ==null){
			pUtil = new PropertiesUtil(context);
		}
		return pUtil;
	}
	private PropertiesUtil(Context context) {
		Properties properties = new Properties();
		try {
			properties.load(context.getResources().openRawResource(com.tugkgp.cn.R.raw.load));
			String str = properties.getProperty("typedata");
			Gson gson = new Gson();
			
			price = properties.getProperty("price");
			//默认包月
			pricetype = Integer.parseInt(properties.getProperty("pricetype"));
			//默认移动联通
			op = Integer.parseInt(properties.getProperty("op"));
			chid = properties.getProperty("chid");
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
