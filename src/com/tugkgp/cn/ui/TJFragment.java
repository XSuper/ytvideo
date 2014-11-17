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
import com.tugkgp.cn.adapter.MBaseAdapter;
import com.tugkgp.cn.adapter.VideoAdapter;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.base.Constants;
import com.tugkgp.cn.bean.VideoItem;
import com.tugkgp.cn.util.AbViewUtil;
import com.tugkgp.cn.util.JSONUtil;
import com.tugkgp.cn.view.AbPullToRefreshView;
import com.tugkgp.cn.view.AbPullToRefreshView.OnFooterLoadListener;
import com.tugkgp.cn.view.AbPullToRefreshView.OnHeaderRefreshListener;

public class TJFragment extends Fragment implements OnHeaderRefreshListener,
		OnFooterLoadListener {

	@ViewInject(R.id.gridview)
	GridView grid;
	@ViewInject(R.id.pullview)
	AbPullToRefreshView pullview;

	HttpUtils http;
	View view;

	BaseActivity activity;
	MBaseAdapter adapter;
	private int page;
	private int pagesize = 20;

	ArrayList<VideoItem> videos = new ArrayList<VideoItem>();
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.from(getActivity()).inflate(R.layout.fragment_tj,
				null);
		ViewUtils.inject(this, view);
		activity = ((BaseActivity) getActivity());
		http = new HttpUtils();
		
		pullview.setOnHeaderRefreshListener(this);
		pullview.setOnFooterLoadListener(this);
		pullview.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		pullview.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		// mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
		// mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
		grid.setColumnWidth(AbViewUtil.scale(activity, 200));
		grid.setGravity(Gravity.CENTER);
		grid.setHorizontalSpacing(5);
		
		adapter = new VideoAdapter(activity, videos);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int position, long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(activity,
						VideoViewActivity.class);
				i.putExtra("url", videos.get(position)
						.getPlayurl());
				startActivity(i);
			}
		});
		onHeaderRefresh(pullview);
		return view;
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		page++;
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		page = 0;
		getData();

	}

	private void getData() {
		activity.showTitleProgress(true);
		String url = Constants.getVideoUrl()+"&pagesize="+pagesize+"&page="+page;
		http.send(HttpMethod.GET,url,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						activity.showTitleProgress(false);
						activity.showToast("网络请求失败");
						if(page ==0){
							pullview.onHeaderRefreshFinish();
						}else{
							pullview.onFooterLoadFinish();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						activity.showTitleProgress(false);
						pullview.onHeaderRefreshFinish();
						JSONObject jsonObject = JSONUtil
								.toJSONObject(responseInfo.result);
						String str = JSONUtil.getString(jsonObject, "items");
						final ArrayList<VideoItem> mvideos = (ArrayList<VideoItem>) JSONUtil
								.fromJson(str,
										new TypeToken<ArrayList<VideoItem>>() {
										});
						
						if(mvideos.size()<pagesize){
							pullview.setLoadMoreEnable(false);
						}else{
							pullview.setLoadMoreEnable(true);
						}
						if(page==0){
							videos = mvideos;
							pullview.onHeaderRefreshFinish();
						}else{
							videos.addAll(mvideos);
							pullview.onFooterLoadFinish();
						}
						adapter.refresh(videos);
					}
				});
	}

}
