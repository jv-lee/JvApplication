package com.jv.sms.mvp.view;

import com.jv.sms.bean.SmsBean;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListView {


    void setSmsBeansAll(LinkedList<SmsBean> list);

    void showSmsListSuccess();

    void showSmsError();

    void deleteSmsSuccess();

    void deleteSmsError();

    void sendSmsLoading(SmsBean smsBean);

    void sendSmsSuccess(SmsBean smsBean);

    void sendSmsError(SmsBean smsBean);

    void deleteThreadSuccess();

    void deleteThreadError();

    void reSendSms(boolean flag, int position);

}
