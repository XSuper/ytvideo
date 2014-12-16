package com.tugkgp.cn.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tugkgp.cn.R;
import com.umeng.analytics.MobclickAgent;


public class AboutFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_about, null);
	}

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("关于我们"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("关于我们"); 
	}
}
