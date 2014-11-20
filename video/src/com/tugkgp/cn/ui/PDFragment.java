package com.tugkgp.cn.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.tugkgp.cn.adapter.VideoAdapter;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.Constants;
import com.tugkgp.cn.bean.Column;
import com.tugkgp.cn.bean.VideoItem;
import com.tugkgp.cn.util.AbViewUtil;
import com.tugkgp.cn.util.JSONUtil;
import com.tugkgp.cn.view.AbPullToRefreshView;
import com.tugkgp.cn.view.AbPullToRefreshView.OnFooterLoadListener;
import com.tugkgp.cn.view.AbPullToRefreshView.OnHeaderRefreshListener;

public class PDFragment extends Fragment {

	@ViewInject(R.id.gridview)
	GridView grid;

	HttpUtils http;
	View view;

	BaseActivity activity;
	MBaseAdapter adapter;

	private ArrayList<Column> columns = new ArrayList<Column>();

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.from(getActivity()).inflate(R.layout.fragment_pd,
				null);
		ViewUtils.inject(this, view);
		activity = ((BaseActivity) getActivity());
		http = new HttpUtils();
		
		// mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
		grid.setColumnWidth(AbViewUtil.scale(activity, 200));
		grid.setGravity(Gravity.CENTER);
		grid.setHorizontalSpacing(5);
		
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
		activity.showTitleProgress(true);
		http.send(HttpMethod.GET,Constants.PDURL,
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
						JSONObject jsonObject = JSONUtil
								.toJSONObject(responseInfo.result);
						String str = JSONUtil.getString(jsonObject, "columns");
						columns = (ArrayList<Column>) JSONUtil
								.fromJson(str,
										new TypeToken<ArrayList<Column>>() {
										});
						adapter.refresh(columns);
					}
				});
	}

}
