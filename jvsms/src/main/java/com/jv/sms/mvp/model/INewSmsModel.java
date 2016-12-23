package com.jv.sms.mvp.model;

import com.jv.sms.bean.ContactsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface INewSmsModel {

    List<ContactsBean> findContactsAll();

}
