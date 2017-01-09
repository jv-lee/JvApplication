package com.jv.sms.mvp.presenter;

import android.app.Activity;
import android.app.PendingIntent;

import com.jv.sms.base.BasePresenter;
import com.jv.sms.bean.SmsBean;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListPresenter extends BasePresenter {


    void findSmsBeansAll(String thread_id);

    void removeSmsById(String id);

    void sendSms(PendingIntent sentPI, String phoneNumber, String content);

    void sendSmsSuccess(SmsBean smsBean);

    void sendSmsError(SmsBean smsBean);

    void saveSmsToDb(SmsBean smsBean, int status);

    void deleteSmsByThreadId(String thread_id);

}
