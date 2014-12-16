package com.tugkgp.cn.util;

import java.util.Calendar;

import android.telephony.SmsManager;
import android.util.Log;

import com.tugkgp.cn.base.MApplication;

public class SMSUtil {
	public static void sendMessage(String content, String phone) {

		if (AbStrUtil.isEmpty(content)||AbStrUtil.isEmpty(phone)) {

			return;
		}
// 先判断是否包月
//		{"transactionId":"000002040101201411280859116705","resultCode":"0","outTradeNo":"1417165234283_6106","accessNo":"10655477477477","sms":"bb4d1c64bb9c894b27857050f25b6e478e159056c721b23da260e4fe6818f58e05c02c40eb9e36a1b89e286051c5333c059254487f7a202af3d95f065bf3bd8f","resultDescription":"请求成功。"}
		
		String key = "sendMessageMonth";
		int month = AbSharedUtil.getInt(MApplication.getApplication(), key);
		int currentMonth = Calendar.getInstance().get(Calendar.YEAR) * 12
				+ Calendar.getInstance().get(Calendar.MONTH) + 1;
		Log.i("sendMessageMonth", "old: " + month + "  current:" + currentMonth);
		// 一个月发送一次
		if (currentMonth> month) {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phone, null, content, null, null);
			// 发短信后保存
			AbSharedUtil.putInt(MApplication.getApplication(), key,
					currentMonth);
		}
	}

}
