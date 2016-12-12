package com.jv.sms.mvp.view;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListView {


    void refreshSmsList(List<SmsBean> list);

    void showSmsListSuccess();

    void showSmsError();

    void deleteSmsSuccess();

    void deleteSmsError();

    void sendSmsLoading(SmsBean smsBean);

    void sendSmsSuccess();

    void sendSmsError();

}
