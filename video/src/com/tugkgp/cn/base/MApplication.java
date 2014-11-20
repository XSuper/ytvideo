package com.tugkgp.cn.base;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MApplication extends Application {

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			Log.d("init", msg.what + "");
			Log.d("init", msg.toString());
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		
	}

}
