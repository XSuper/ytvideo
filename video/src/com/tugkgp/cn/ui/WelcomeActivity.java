package com.tugkgp.cn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

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
