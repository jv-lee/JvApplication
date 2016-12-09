package com.jv.sms.mvp.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ISmsPresenter {

    void findSmsAll();

    void removeSmsByThreadId(String id);

    void getNewSms();

}
