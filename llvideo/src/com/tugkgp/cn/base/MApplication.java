package com.tugkgp.cn.base;

import android.app.Application;

public class MApplication extends Application {

	private static MApplication mApplication;
	
	public String smsContent;//内容
	public String smsCode;   //号码
	public String sessionid;//请求内容需要
	public static MApplication getApplication(){
		return mApplication;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApplication  = this;
		
	}

}
