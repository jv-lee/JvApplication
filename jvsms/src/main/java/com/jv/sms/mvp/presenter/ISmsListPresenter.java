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

    void sendSms(PendingIntent sentPI, String phoneNumber, String content, long time);

    void sendSmsSuccess(SmsBean smsBean);

    void sendSmsError(SmsBean smsBean);

    void deleteSmsByThreadId(String thread_id);

    void reSendSms(String id, int position); //通过id 删除原发送失败短信 重新发起当前短信 填充时间

}
