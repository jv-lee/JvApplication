package com.jv.sms.constant;

import com.jv.sms.R;
import com.jv.sms.entity.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class Constant {

    public static final int SMS_STATUS_NOT_READ = 0x00;
    public static final int SMS_STATUS_IS_READ = 0x01;
    public static final int SMS_STATUS_RECEIVER = 0x01;
    public static final int SMS_STATUS_SEND = 0x02;

    public static final String RX_CODE_DELETE_THREAD_ID = "rx_code_delete_thread_id";
    public static final String RX_CODE_UPDATE_MESSAGE = "rx_code_update_message";

    public static final int SMS_STATUS_ERROR = 128;

    public static String THIS_SMS_FRAGMENT_FLAG = "";

    //记录设置 是否接受通知
    public static final String SETTINGS_NOTIFICATION = "has_receiver_notification";
    public static final String SETTINGS_VOICE = "settings_voice";
    public static final String SETTINGS_SHOCK = "settings_shock";

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

    public static String[] settingsStr = {"默认短信应用", "接受通知", "通知提示音", "听到短信发送提示音", "震动", "您当前所在国家", "手机号码"};

}
