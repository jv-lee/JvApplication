package com.jv.sms.mvp.presenter;

import android.app.Activity;
import android.app.PendingIntent;

import com.jv.sms.base.BasePresenter;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListPresenter extends BasePresenter {


    void refreshSmsList(String thread_id);

    void removeSmsById(String id);

    void sendSms(PendingIntent sentPI, String phoneNumber, String content);

    void sendSmsSuccess();

    void sendSmsError();

}
