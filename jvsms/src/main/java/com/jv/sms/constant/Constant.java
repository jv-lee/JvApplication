package com.jv.sms.constant;

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

    //记录设置 是否接受通知
    public static final String SETTINGS_NOTIFICATION = "has_receiver_notification";
    public static final String SETTINGS_VOICE = "settings_voice";
    public static final String SETTINGS_SHOCK = "settings_shock";

}
