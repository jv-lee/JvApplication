package com.jv.sms.mvp.presenter;

import android.content.Context;

import com.jv.sms.bean.SmsBean;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ISmsPresenter {

    void findSmsAll();

    void removeSmsByThreadId(String id);

    void getNewSms();

    void updateSmsState(SmsBean smsBean);

}
