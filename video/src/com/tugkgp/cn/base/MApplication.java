package com.tugkgp.cn.base;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MApplication extends Application {

	private static MApplication mApplication;
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
