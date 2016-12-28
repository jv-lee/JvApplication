package com.jv.sms.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.jv.sms.R;
import com.jv.sms.activity.SmsActivity;
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.bean.SmsListUiFlagBean;
import com.jv.sms.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class NotificationUtils {

    public static List<SmsBean> smsList = new ArrayList<>();
    public static int smsNum = 0;

    public static void clearNum() {
        smsList.clear();
        smsNum = 0;
    }

    public static void showNotification(String phoneNumber, String content) {
        //SmsList列表在栈顶时 进入判断
        if (isMainActivityTop()) {
            //当前浏览短信号码不等于"" 的情况下 进入
            if (!JvApplication.THIS_SMS_FRAGMENT_FLAG.equals("")) {
                //当前获取短信号码 为当前浏览短信 直接return 不发送通知
                if (phoneNumber.equals(JvApplication.THIS_SMS_FRAGMENT_FLAG)) {
                    return;
                }
            }
        }

        SmsBean smsBean = new SmsBean();
        if (smsList.size() == 0) {
            smsBean.setPhoneNumber(phoneNumber);
            smsBean.setSmsBody(phoneNumber + "\n" + content);
            smsList.add(smsBean);
            smsNum = 1;
        } else if (smsList.size() >= 1) {
            SmsBean smsBean1 = smsList.get(0);
            String phoneNumber1 = smsBean1.getPhoneNumber();
            smsBean1.setSmsBody(phoneNumber1 + "等...");
            smsList.set(0, smsBean1);
            smsNum++;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(JvApplication.getInstance().getResources(), R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(JvApplication.getInstance(), SmsActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent contentIntent = PendingIntent.getActivity(JvApplication.getInstance(), 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) JvApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(JvApplication.getInstance());
        nBuilder.setContentTitle("收到" + smsNum + "条短消息")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(smsList.get(0).getSmsBody())
                .setTicker(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_call_back, "点击回复", null) //添加点击Action事件
        ;

        notificationManager.notify(1, nBuilder.build());
    }

    /**
     * 判断mainactivity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    private static boolean isMainActivityTop() {
        ActivityManager manager = (ActivityManager) JvApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(SmsListActivity.class.getName());
    }

}
