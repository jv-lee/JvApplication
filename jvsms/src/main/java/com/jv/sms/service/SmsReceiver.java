package com.jv.sms.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.jv.sms.bean.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.constant.Constant;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.NotificationUtils;
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

        Toast.makeText(context, "进入", Toast.LENGTH_SHORT).show();

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

                //获取短信内容并存入数据库
                for (SmsMessage curMsg : msg) {
                    sms.setDate(TimeUtils.milliseconds2String(curMsg.getTimestampMillis()));
                    sms.setPhoneNumber(curMsg.getDisplayOriginatingAddress());
                    sms.setSmsBody(curMsg.getDisplayMessageBody());
                    sms.setType(SmsBean.Type.RECEIVE);
                    sms.setReadType(SmsBean.ReadType.NOT_READ);
                    sms.setName(SmsUtils.getPeopleNameFromPerson(sms.getPhoneNumber(), context));

                    //添加接受短信至数据库
                    SmsUtils.addSmsToDB(context, sms.getPhoneNumber(), sms.getSmsBody(), curMsg.getTimestampMillis(), Constant.SMS_STATUS_NOT_READ, Constant.SMS_STATUS_RECEIVER);
                }

                //通过RxBus发送新短信通知
                RxBus.getInstance().post(new EventBase(sms.getPhoneNumber(), sms));

                NotificationUtils.showNotification(sms.getName(), sms.getSmsBody());
            }
        }
    }

}
