package com.tugkgp.cn.base;

import java.lang.reflect.Field;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.thromer.frame.local.BaseApiInterface;
import com.thromer.frame.utra.trace;
import com.tugkgp.cn.util.AbAppUtil;

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

		WebView webview = new WebView(this);
		webview.layout(0, 0, 0, 0);
		WebSettings settings = webview.getSettings();
		String ua = settings.getUserAgentString();

		String channleid = "101000000030001";
		String appid = "5e4fb7309774456c8b4914b8ceadb50c";
		String packagename = "com.tugkgp.cn";
		String key = "fd95ddadcca905d2a68e512f4e552b69";
		String imsi = AbAppUtil.getIMSI(this);
		Log.i("UA", ua);
		Log.i("imsi", imsi);

		BaseApiInterface.getCoreSdkIntance();
		trace log = trace.log();
		Field field =null;
		try {
			field = trace.class.getDeclaredField("isLog");
			field.setAccessible(true);
			field.set(log, true);
			
			BaseApiInterface.class.getDeclaredField("log");
			field.setAccessible(true);
			field.set(BaseApiInterface.getCoreSdkIntance(), log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BaseApiInterface.getCoreSdkIntance().init(getBaseContext(), channleid,
				"GT-I9308", "1", imsi, 1, key, appid);
	}

}
