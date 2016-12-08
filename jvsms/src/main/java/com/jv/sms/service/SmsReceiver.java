package com.jv.sms.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.bean.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.SmsUtils;
import com.jv.sms.utils.TimeUtils;

/**
 * Created by Administrator on 2016/12/6.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {


        if (SMS_DELIVER.equals(intent.getAction())) {
            StringBuilder builder = new StringBuilder();
            Bundle bundle = intent.getExtras();

            SmsBean sms = new SmsBean();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                //获取短信内容并存入数据库
                for (SmsMessage curMsg : msg) {
                    sms.setDate(TimeUtils.milliseconds2String(curMsg.getTimestampMillis()));
                    sms.setPhoneNumber(curMsg.getDisplayOriginatingAddress());
                    sms.setSmsBody(curMsg.getDisplayMessageBody());
                    sms.setType(SmsBean.Type.RECEIVE);
                    sms.setName(SmsUtils.getPeopleNameFromPerson(sms.getPhoneNumber(), context));
                    addSmsToDB(context, curMsg.getDisplayOriginatingAddress(), curMsg.getDisplayMessageBody(), curMsg.getTimestampMillis());
                }

                //通过RxBus发送新短信通知
                RxBus.getInstance().post(new EventBase(sms.getPhoneNumber(), sms));
            }
        }
    }


    /**
     * 添加到系统短信数据库
     */
    private void addSmsToDB(Context context, String address, String content, long date) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("read", 0);//0为未读信息
        values.put("type", 1);//1为收件箱信息
        values.put("address", address);
        values.put("body", content);
        context.getContentResolver().insert(Uri.parse("content://sms"), values);
    }

}
