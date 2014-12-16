package com.tugkgp.cn.ui;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.slidingmenu.lib.SlidingMenu;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.ActivityTask;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.Constants;
import com.tugkgp.cn.base.MApplication;
import com.tugkgp.cn.util.JSONUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainAcitivity extends BaseActivity implements
		OnCheckedChangeListener {

	SlidingMenu menu;

	RadioGroup group;
	RadioButton b_tj, b_pd;

	int page = 0;
	int pagesize = 20;

	int currentIndex = R.id.tj;

	TextView title;
	TextView smalltitle;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		UmengUpdateAgent.update(this);

		title = (TextView) findViewById(R.id.title_title);
		smalltitle = (TextView) findViewById(R.id.title_smalltitle);
		progress = (ProgressBar) findViewById(R.id.title_progress);

		title.setText(getResources().getString(R.string.app_name));
		smalltitle.setText("资费标准：8元/月");
		findViewById(R.id.title_menu).setOnClickListener(new OnClickListener() {

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
							// 友盟保存统计
							MobclickAgent.onKillProcess(getApplication());
							ActivityTask.getActivityTask().AppExit(
									getApplicationContext());
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
	public boolean showTitleProgress(boolean show) {
		progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
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
		case R.id.about:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_acivity, new AboutFragment()).commit();
			break;
		}

		menu.showContent();

	}

}
