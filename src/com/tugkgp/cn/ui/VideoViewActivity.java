package com.tugkgp.cn.ui;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tugkgp.cn.R;
import com.tugkgp.cn.base.BaseActivity;

public class VideoViewActivity extends BaseActivity {
	VideoView video;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_videoview);
		showProgressDialog(true);
		video = (VideoView) findViewById(R.id.videoview);
		video.setMediaController(new MediaController(this));
		String playUrl = getIntent().getStringExtra("url");
		Uri uri = Uri.parse(playUrl);
		video.setVideoURI(uri);
		video.requestFocus();
		video.start();
		video.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				showProgressDialog(false);
			}
		});
		video.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(video!=null&&video.isPlaying()){
			video.pause();
		}
		super.onBackPressed();
	}

}
