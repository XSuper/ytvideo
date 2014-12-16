package com.tugkgp.cn.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tugkgp.cn.R;
import com.tugkgp.cn.adapter.ColumnAdapter;
import com.tugkgp.cn.adapter.MBaseAdapter;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.Constants;
import com.tugkgp.cn.bean.Column;
import com.tugkgp.cn.util.AbAppUtil;
import com.tugkgp.cn.util.AbStrUtil;
import com.tugkgp.cn.util.AbViewUtil;
import com.tugkgp.cn.util.JSONUtil;
import com.tugkgp.cn.util.PropertiesUtil;
import com.tugkgp.cn.util.SIMUtil;
import com.umeng.analytics.MobclickAgent;

public class PDFragment extends Fragment {

	@ViewInject(R.id.gridview)
	GridView grid;

	HttpUtils http;
	View view;

	BaseActivity activity;
	MBaseAdapter adapter;
	

	private ArrayList<Column> columns = new ArrayList<Column>();

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("频道页面"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("频道页面"); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.from(getActivity()).inflate(R.layout.fragment_pd,
				null);
		activity = ((BaseActivity) getActivity());
		
		ViewUtils.inject(this, view);
		http = new HttpUtils();
		
		// mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
		grid.setColumnWidth(AbViewUtil.scale(activity, 200));
		grid.setGravity(Gravity.CENTER);
		grid.setHorizontalSpacing(5);
		if(columns==null)columns = new ArrayList<Column>();
		adapter = new ColumnAdapter(activity, columns);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int position, long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(activity,
						PDVideosActivity.class);
				i.putExtra("columnid", columns.get(position)
						.getColumnid());
				i.putExtra("title", columns.get(position).getTitle());
				startActivity(i);
			}
		});
		

		getData();
		return view;
	}

	

	

	private void getData() {
		int op = SIMUtil.getProviders(getActivity());
		if(op==0)op=2;
		String imsi = AbAppUtil.getIMSI(getActivity());
		String imei =((TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		activity.showTitleProgress(true);
		http.send(HttpMethod.GET,"http://client.wl.tudou.com/signin_complex?ci=&imei="+imei+"&imsi="+imsi+"&ua=GT-I9103&ver=2.0&pf=04&subpf=000&projid=1017&op="+op+"&subchid=0000&chid="+PropertiesUtil.getInstance(activity).chid+"&sw=720&sh=1280&pn=+",
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						activity.showTitleProgress(false);
						activity.showToast("网络请求失败");
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						activity.showTitleProgress(false);
						Log.v("result", responseInfo.result);
						JSONObject jsonObject = JSONUtil
								.toJSONObject(responseInfo.result);
						String str = JSONUtil.getString(jsonObject, "columns");
						if(AbStrUtil.isEmpty(str)){
							activity.showToast("网络请求异常");
							return;
						}
						columns = (ArrayList<Column>) JSONUtil
								.fromJson(str,
										new TypeToken<ArrayList<Column>>() {
										});
						if(columns==null){
							activity.showToast("网络请求异常");
							return;
						}
						adapter.refresh(columns);
					}
				});
	}

}