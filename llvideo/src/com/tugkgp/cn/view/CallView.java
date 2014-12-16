package com.tugkgp.cn.view;

import com.tugkgp.cn.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CallView extends LinearLayout{

	public CallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	

	public CallView(Context context) {
		super(context);
		init(context);
	}

	private void init(final Context context) {

		this.setBackgroundResource(R.drawable.telephone);
		
		//添加拨打电话事件
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://4007766368"));    
				context.startActivity(intent); 
			}
		});
	}
	
}
