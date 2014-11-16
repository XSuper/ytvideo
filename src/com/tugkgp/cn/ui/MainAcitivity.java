package com.tugkgp.cn.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.thromer.frame.local.BaseApiInterface;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.ActivityTask;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.util.AbAppUtil;

public class MainAcitivity extends BaseActivity implements
		OnCheckedChangeListener {

	SlidingMenu menu;

	RadioGroup group;
	RadioButton b_tj, b_pd;

	int page = 0;
	int pagesize = 20;

	int currentIndex = R.id.tj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentViewWithTitleBar(R.layout.activity_main);
		ViewUtils.inject(this);
		mTitleBar.setTitleText(R.string.app_name);
		mTitleBar.setLogo(R.drawable.btn);
		mTitleBar.getLogoView().setBackgroundResource(R.drawable.btn);
		mTitleBar.setLogoOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
			}
		});

		
		

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setShadowDrawable(R.drawable.ic_launcher);
		// menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setBehindWidthRes(R.dimen.menu_width);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu);

		group = (RadioGroup) menu.findViewById(R.id.menugroup);
		group.setOnCheckedChangeListener(this);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_acivity, new TJFragment()).commit();
		// SlidingMenu的配置

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			menu.toggle();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (menu.isMenuShowing()) {

			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("确定退出程序？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							ActivityTask.getActivityTask().AppExit(getApplicationContext());
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			AlertDialog mAlertDialog = builder.create();
			mAlertDialog.show();
		}

		else {
			menu.showMenu();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		page = 0;
		currentIndex = checkedId;
		switch (checkedId) {
		case R.id.pd:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_acivity, new PDFragment()).commit();
			break;

		case R.id.tj:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_acivity, new TJFragment()).commit();
			break;
		}

		menu.showContent();

	}
}
