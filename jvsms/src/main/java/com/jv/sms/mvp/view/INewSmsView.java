package com.jv.sms.mvp.view;

import com.jv.sms.bean.ContactsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public interface INewSmsView {

    void setContactsAll(List<ContactsBean> list);

    void findContactsAllError();

}