package com.jv.sms.mvp.model;

import android.content.Context;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ISmsModel {

    List<SmsBean> findSmsAll(Context context);

    boolean removeSmsByThreadId(String id);

    SmsBean getNewSms();

    void updateSmsState(SmsBean smsBean);

    boolean insertSmsDB(String[] ids);

    boolean deleteSmsDB(String[] ids);

}
