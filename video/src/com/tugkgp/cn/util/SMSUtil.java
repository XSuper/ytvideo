package com.tugkgp.cn.util;

import java.util.Calendar;
import java.util.Date;

import com.tugkgp.cn.base.MApplication;

import android.telephony.SmsManager;
import android.util.Log;

public class SMSUtil {
	public static void sendMessage(String content,String phone) {
//		String content = "HDGKGP2";// 短信内容
//		String phone = "106588309919";// 电话号码
		
		//先判断是否包月
		
		String key = "sendMessageMonth";
		int month = AbSharedUtil.getInt(MApplication.getApplication(), key);
		int currentMonth = Calendar.getInstance().get(Calendar.YEAR)*12+Calendar.getInstance().get(Calendar.MONTH)+1;
		Log.i("sendMessageMonth", "old: "+month+"  current:"+currentMonth);
		if(currentMonth>month){
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone, null, content, null, null);
			//发短信后保存
			AbSharedUtil.putInt(MApplication.getApplication(), key, currentMonth);
		}
	}

}
