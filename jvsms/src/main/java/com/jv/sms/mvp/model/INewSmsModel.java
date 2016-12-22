package com.jv.sms.mvp.model;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface INewSmsModel {

    List<SmsBean> findContactsAll();

}
