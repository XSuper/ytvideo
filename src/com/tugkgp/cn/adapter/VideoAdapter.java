package com.tugkgp.cn.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.tugkgp.cn.R;
import com.tugkgp.cn.base.BaseActivity;
import com.tugkgp.cn.bean.VideoItem;

public class VideoAdapter extends MBaseAdapter {

	BitmapUtils bitmapUtils;
	
	public VideoAdapter(BaseActivity activity,ArrayList list) {
		super();
		// TODO Auto-generated constructor stub
		this.context = activity;
		this.datas = list;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHoder hoder;
		if(convertView ==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_video, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			
			hoder = new ViewHoder();
			hoder.img = img;
			hoder.txt = txt;
			convertView.setTag(hoder);
		}else{
			hoder = (ViewHoder) convertView.getTag();
		}
		VideoItem videoItem = (VideoItem) datas.get(position);
		
		bitmapUtils.display(hoder.img, videoItem.getPicurl());
		hoder.txt.setText(videoItem.getTitle());
		return convertView;
	}

}

class ViewHoder{
	ImageView img;
	TextView txt;
}
