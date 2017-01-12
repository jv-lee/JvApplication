package com.jv.sms.mvp.view;

import com.jv.sms.bean.SmsBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ISmsView {

    void setData(LinkedList<SmsBean> beanList);

    void setDataError();

    void setDataSuccess();

    void removeDataError();

    void removeDataSuccess(int position);

    void setNewSms(SmsBean sms);

    void insertSmsNotificationSuccess();

    void insertSmsNotificationError();


}
