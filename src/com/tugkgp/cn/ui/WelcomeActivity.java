package com.tugkgp.cn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thromer.frame.kit.EventObsListener;
import com.thromer.frame.local.BaseApiInterface;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.BaseActivity;

public class WelcomeActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LinearLayout v = new LinearLayout(this);
		v.setBackgroundResource(R.drawable.welcome);
		setContentView(v, layoutParamsFF);
		
		BaseApiInterface.getCoreSdkIntance().setEventObsListener(new EventObsListener() {
			
			@Override
			public void onPublicExecute(int arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.e("mlistener", arg1);
				
			}
		});
		//BaseApiInterface.getCoreSdkIntance().doCheckLogin(getBaseContext());
		BaseApiInterface.getCoreSdkIntance().queryTokenStatus(getBaseContext());
		
		AlphaAnimation a = new AlphaAnimation(0.7f,1f);
		a.setDuration(15000);
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
				
				BaseApiInterface.getCoreSdkIntance().doCheckLogin(getApplicationContext());
				Intent i = new Intent(getBaseContext(), MainAcitivity.class);
				startActivity(i);
				finish();
				
			}
		});
	}

}
