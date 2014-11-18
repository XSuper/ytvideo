package com.tugkgp.cn.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tugkgp.cn.R;
import com.tugkgp.cn.util.AbAppUtil;
import com.tugkgp.cn.view.TitleBar;



public class BaseActivity extends FragmentActivity{
	public LayoutInflater mInflater;
	public View Layout_base;
	public RelativeLayout contentLayout;
	public TitleBar mTitleBar;
	public LayoutParams layoutParamsFF;
	public LayoutParams layoutParamsFW;
	public LayoutParams layoutParamsWF;
	public LayoutParams layoutParamsWW;
	
	/*progress 相关*/
	ProgressDialog dialog;
	
	private int focusCount = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = LayoutInflater.from(this);
		
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		ActivityTask.getActivityTask().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityTask.getActivityTask().finishActivity(this);
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(focusCount==0){
			onActivityLoad();
		}
		focusCount++;
	}
	public void onActivityLoad() {
		// TODO Auto-generated method stub
		
	}

	//为了得到Layout_base
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		setContentView(mInflater.inflate(layoutResID, null));
		
	}
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		Layout_base = (ViewGroup) view;
	}
	@Override
	public void setContentView(View view,
			android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
		Layout_base = (ViewGroup) view;
	}
	/**
	 *  activity显示titlebar
	 * @param layoutResID
	 */
	public void setContentViewWithTitleBar(int layoutResID){
		setContentViewWithTitleBar(mInflater.inflate(layoutResID, null));
	}
	public void setContentViewWithTitleBar(View contentView){
		//主标题栏
		mTitleBar = new TitleBar(this);
		
		//初始化TitleBar
		//mTitleBar.setLogo(R.drawable.common_btn_home);
		mTitleBar.setGravity(Gravity.CENTER);
		int toph = AbAppUtil.getDisplayMetrics(this).heightPixels/10;
		
		//LayoutParams params = new LayoutParams(toph, toph);
		//mTitleBar.getLogoView().setLayoutParams(params);
		mTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mTitleBar.setLogoLine(R.color.black);
		mTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);
		
		//最外层布局
		RelativeLayout Layout_base = new RelativeLayout(this);
		Layout_base.setBackgroundColor(Color.rgb(255, 255, 255));
		//内容布局
		contentLayout = new RelativeLayout(this);
		contentLayout.setPadding(0, 0, 0, 0);
		
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,layoutParamsFF);
		
		//副标题栏
		
        //填入View
		Layout_base.addView(mTitleBar,layoutParamsFW);
		
		
		RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsFW1.addRule(RelativeLayout.BELOW, mTitleBar.getId());
		Layout_base.addView(contentLayout, layoutParamsFW1);
		
		//设置ContentView
        setContentView(Layout_base,layoutParamsFF);
	}
	
	/**
	 * 
	 * @return 是否成功显示
	 */
	public boolean showTitleProgress(boolean show){
		if(mTitleBar==null){
			return false;
		}
		if(show){
			mTitleBar.getRightLayout().removeAllViews();
			ProgressBar progressBar = new ProgressBar(this);
			//android.view.ViewGroup.LayoutParams params = new LayoutParams(, 50);
			progressBar.setLayoutParams(layoutParamsWW );
			mTitleBar.getRightLayout().addView(progressBar);
			mTitleBar.getRightLayout().setVisibility(View.VISIBLE);
		}else{
			mTitleBar.getRightLayout().removeAllViews();
			mTitleBar.getRightLayout().setVisibility(View.GONE);
		}
		return true;
	}
	
	public void showToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}
	

	public void showProgressDialog(boolean show){
		if(dialog==null){
			dialog=new ProgressDialog(this);
			dialog.setMessage("正在加载");
		}
		if(show){
			dialog.show();
		}else{
			dialog.dismiss();
		}
	}
}
