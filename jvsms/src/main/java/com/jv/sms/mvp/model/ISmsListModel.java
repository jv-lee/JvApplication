package com.jv.sms.mvp.model;

import android.app.PendingIntent;

import com.jv.sms.bean.SmsBean;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListModel {

    boolean deleteSmsListById(String id);

    LinkedList<SmsBean> refreshSmsList(String thread_id);

    SmsBean sendSms(PendingIntent sentPI, String phoneNumber, String content);

    boolean removeSmsByThreadId(String thread_id);
}
