package com.jv.sms.mvp.model;

import android.app.Activity;
import android.content.Context;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListModel {

    boolean deleteSmsListById(String id);

    List<SmsBean> refreshSmsList(String thread_id);

}
