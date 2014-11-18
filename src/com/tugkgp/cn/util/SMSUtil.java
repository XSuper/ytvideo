package com.tugkgp.cn.util;

import android.telephony.SmsManager;

public class SMSUtil {
	public static void sendMessage() {
		String content = "HDGKGP2";// 短信内容
		String phone = "10658830991";// 电话号码
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, content, null, null);
	}

}
