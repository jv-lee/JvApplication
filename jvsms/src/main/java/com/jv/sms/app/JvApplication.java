package com.jv.sms.app;

import android.app.Application;
import android.content.Context;

import com.jv.sms.R;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class JvApplication extends Application {

    private static Context mContext;

    public static String THIS_SMS_FRAGMENT_FLAG = "";

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPreferencesUtils.config(this);
    }

    public static int[] icon_theme_colors = {R.color.colorPrimary1, R.color.colorPrimary2,
            R.color.colorPrimary3, R.color.colorPrimary4,
            R.color.colorPrimary5, R.color.colorPrimary6,
            R.color.colorPrimary7, R.color.colorPrimary8,
            R.color.colorPrimary9};

    public static int[] icon_theme_darkColors = {R.color.colorPrimaryDark1, R.color.colorPrimaryDark2,
            R.color.colorPrimaryDark3, R.color.colorPrimaryDark4,
            R.color.colorPrimaryDark5, R.color.colorPrimaryDark6,
            R.color.colorPrimaryDark7, R.color.colorPrimaryDark8,
            R.color.colorPrimaryDark9};

    public static int[] themes = {R.style.AppTheme1_NoActionBar, R.style.AppTheme2_NoActionBar,
            R.style.AppTheme3_NoActionBar, R.style.AppTheme4_NoActionBar,
            R.style.AppTheme5_NoActionBar, R.style.AppTheme6_NoActionBar,
            R.style.AppTheme7_NoActionBar, R.style.AppTheme8_NoActionBar,
            R.style.AppTheme9_NoActionBar};

    public static int themeId = 0;

    public static List<SmsBean> smsBeans;
    public static String text = "";

    public static SmsBean smsBean; //当前发送中短信

}
