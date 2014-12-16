package com.tugkgp.cn.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.MApplication;
import com.tugkgp.cn.pay.SMSPay;
import com.tugkgp.cn.util.AbAppUtil;
import com.tugkgp.cn.util.JSONUtil;
import com.tugkgp.cn.util.PropertiesUtil;
import com.tugkgp.cn.util.SIMUtil;

public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout v = new LinearLayout(this);
		v.setBackgroundResource(R.drawable.welcome);
		setContentView(v, layoutParamsFF);

		SMSPay.getInstance(this).requestSMS();
		/**
		 * 签到  获取sessionid
		 */
		
		int op = SIMUtil.getProviders(this);
		if(op==0)op=2;
		String imsi = AbAppUtil.getIMSI(this);
		String imei =((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    	String murl = "http://client.wl.tudou.com/signin?ci=&imei="+imei+"&imsi="+imsi+"&ua=MI+2SC&ver=1.1.7&pf=04&subpf=000&projid=1017&op="+op+"&chid="+PropertiesUtil.getInstance(this).chid+"&subchid=0000&sw=720&sh=1280&pn=";
    	HttpUtils net = new HttpUtils(5000);
		net.send(HttpMethod.GET, murl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				responseInfo.result;
				JSONObject jo = JSONUtil.toJSONObject(responseInfo.result);
				MApplication.getApplication().sessionid = JSONUtil.getString(jo, "sessionid");
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	

}
