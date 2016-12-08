package com.jv.sms.mvp.presenter;

import android.app.Activity;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListPresenter {


    void refreshSmsList(String thread_id);

    void removeSmsById(String id);

}
