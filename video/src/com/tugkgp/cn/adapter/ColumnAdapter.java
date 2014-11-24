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
import com.tugkgp.cn.bean.Column;

public class ColumnAdapter extends MBaseAdapter {

	BitmapUtils bitmapUtils;
	
	public ColumnAdapter(BaseActivity activity,ArrayList list) {
		super();
		// TODO Auto-generated constructor stub
		this.context = activity;
		this.datas = list;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.mr_pic);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.mr_pic);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MViewHoder hoder;
		if(convertView ==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_pd, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			
			hoder = new MViewHoder();
			hoder.img = img;
			hoder.txt = txt;
			convertView.setTag(hoder);
		}else{
			hoder = (MViewHoder) convertView.getTag();
		}
		Column Column = (Column) datas.get(position);
		bitmapUtils.display(hoder.img, Column.getIconurl());
		hoder.txt.setText(Column.getTitle());
		return convertView;
	}

}

class MViewHoder{
	ImageView img;
	TextView txt;
}
