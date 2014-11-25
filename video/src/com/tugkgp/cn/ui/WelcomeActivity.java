package com.tugkgp.cn.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.MApplication;
import com.tugkgp.cn.util.JSONUtil;
import com.tugkgp.cn.util.SIMUtil;
import com.tugkgp.cn.util.SMSUtil;

public class WelcomeActivity extends BaseActivity{
	int type = 1;//默认移动
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LinearLayout v = new LinearLayout(this);
		v.setBackgroundResource(R.drawable.welcome);
		setContentView(v, layoutParamsFF);
		
		//获取运营商类型
		type = SIMUtil.getProviders(this);
		Log.v("sms-info", "type : "+type+"   ip :"+SIMUtil.getLocalIpAddress());
		switch (type) {
		case 1://移动
			String content = "HDGKGP2";// 短信内容
			String phone = "106588309919";// 电话号码
			MApplication.getApplication().smsContent = content;
			MApplication.getApplication().smsCode = phone;
			animal(v);
			break;
		case 2://联通
			String chid = "";
			String ip = SIMUtil.getLocalIpAddress();
			String url = "http://mdo2.wl.tudou.com/wl/sms_fee_req.jsp?chid="+chid+"&opid=2&ip="+ip+"&price=12";

			requestSMS(url);
			break;
		case 3://电信
			
			animal(v);
			break;

		
		}
		
		
		
	}

	
	private void requestSMS(String url) {
		HttpUtils net = new HttpUtils();
		net.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject jsonObject = JSONUtil.toJSONObject(responseInfo.result);
				//访问接口成功
				if("0".equals(JSONUtil.getString(jsonObject, "resultCode"))){
					MApplication.getApplication().smsCode = JSONUtil.getString(jsonObject, "accessNo");
					MApplication.getApplication().smsContent = JSONUtil.getString(jsonObject, "sms");
				}
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	private void animal(LinearLayout v) {
		AlphaAnimation a = new AlphaAnimation(0.7f,1f);
		a.setDuration(2000);
		v.setAnimation(a);
		a.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
				
			}
		});
	}

}
