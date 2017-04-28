package com.jv.sms.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2016/12/21.
 * 所需权限 <uses-permission android:name="android.permission.CALL_PHONE" />
 */
public class TelUtils {


    /**
     * 跳转到拨号界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void sendTel1(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 直接拨打该号码
     *
     * @param context
     * @param phoneNumber
     */
    public static void sendTel2(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

}
