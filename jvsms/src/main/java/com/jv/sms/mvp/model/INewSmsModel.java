package com.jv.sms.mvp.model;

import com.jv.sms.bean.ContactsBean;
import com.jv.sms.bean.LinkmanBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface INewSmsModel {

    /**
     * 用于新增短信 联系人列表显示
     *
     * @return
     */
    List<ContactsBean> findContactsAll();

    /**
     * 用于新增短信 联系人输入时搜索提示显示列表
     *
     * @return
     */
    List<LinkmanBean> findLinkmanAll();

}
