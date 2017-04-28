package com.jv.sms.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.jv.sms.R;
import com.jv.sms.constant.Constant;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.ui.content.ContentActivity;
import com.jv.sms.ui.sms.SmsActivity;

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

    public static void showNotification(Context context, String phoneNumber, String content) {
        //SmsList列表在栈顶时 进入判断
        if (isMainActivityTop(context)) {
            //当前浏览短信号码不等于"" 的情况下 进入
            if (!Constant.THIS_SMS_FRAGMENT_FLAG.equals("")) {
                //当前获取短信号码 为当前浏览短信 直接return 不发送通知
                if (phoneNumber.equals(Constant.THIS_SMS_FRAGMENT_FLAG)) {
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

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(context, SmsActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context);
        nBuilder.setContentTitle("收到" + smsNum + "条短消息")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(smsList.get(0).getSmsBody())
                .setTicker(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
//                .setDefaults(Notification.DEFAULT_VIBRATE) //设置默认提示震动 和 提示音
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_call_back, "点击回复", null); //添加点击Action事件

        //设置提示音
        if ((boolean) SPHelper.get(Constant.SETTINGS_VOICE, true)) {
            nBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notif));
        }
        //设置震动
        if ((boolean) SPHelper.get(Constant.SETTINGS_SHOCK, true)) {
            nBuilder.setVibrate(new long[]{0, 1000, 0, 0});
        }
        notificationManager.notify(1, nBuilder.build());
    }

    /**
     * 判断mainactivity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    private static boolean isMainActivityTop(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(ContentActivity.class.getName());
    }

}
