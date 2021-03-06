package com.jv.sms.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jv.sms.Config;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.rx.EventBase;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.NotificationUtil;
import com.jv.sms.utils.SPUtil;
import com.jv.sms.utils.SmsUtil;
import com.jv.sms.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/12/6.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SMS_DELIVER.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();

            SmsBean sms = new SmsBean();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                SmsMessage[] msg = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }

                List<Long> times = new ArrayList<>();
                //获取短信内容并存入数据库
                for (SmsMessage curMsg : msg) {
                    long time = System.currentTimeMillis();
                    times.add(time);
                    sms.setDate(TimeUtil.milliseconds2String(time));
                    sms.setPhoneNumber(curMsg.getDisplayOriginatingAddress());
                    sms.setSmsBody(curMsg.getDisplayMessageBody());
                    sms.setType(SmsBean.Type.RECEIVE);
                    sms.setReadType(SmsBean.ReadType.NOT_READ);
                    sms.setName(SmsUtil.getPeopleNameFromPerson(sms.getPhoneNumber(), context));

                    //添加接受短信至数据库
                    SmsUtil.addSmsToDB(context, sms.getPhoneNumber(), sms.getSmsBody(), time, Config.SMS_STATUS_NOT_READ, Config.SMS_STATUS_RECEIVER, -1);
                }


                ContentResolver cr = context.getContentResolver();
                Cursor cursor = null;
                SmsBean smsBean = null;
                Log.i("SmsReceiver", times.size() + "");
                for (int i = 0; i < times.size(); i++) {
                    cursor = cr.query(Uri.parse("content://sms/"), new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"},
                            "date = ?", new String[]{times.get(i) + ""}, null);

                    if (cursor.moveToFirst()) {
                        smsBean = SmsUtil.simpleSmsBean(cursor, context);
                    }

                    Log.i("SmsReceiver", smsBean.getSmsBody());
                    //通过RxBus发送新短信通知
                    RxBus.getInstance().post(new EventBase(smsBean.getPhoneNumber(), smsBean));

                }
                SPUtil.getInstance(context);
                if ((boolean) SPUtil.get(Config.SETTINGS_NOTIFICATION, true)) {
                    NotificationUtil.showNotification(context, sms.getName(), sms.getSmsBody());
                }
            }
        }
    }

}
