package com.jv.sms.ui.newsms;

import com.jv.sms.base.mvp.IModel;
import com.jv.sms.base.mvp.IPresenter;
import com.jv.sms.base.mvp.IView;
import com.jv.sms.entity.ContactsBean;
import com.jv.sms.entity.LinkmanBean;
import com.jv.sms.entity.SmsBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface NewSmsContract {

    interface View extends IView{
        void setContactsAll(List<ContactsBean> list);

        void findContactsAllError();

        void setLinkmanAll(List<LinkmanBean> list);

        void findLinkmanAllError();

        void startListSmsBySms(SmsBean smsBean);
    }

    interface Model extends IModel{
        /**
         * 用于新增短信 联系人列表显示
         *
         * @return
         */
        Observable<List<ContactsBean>> findContactsAll();

        /**
         * 用于新增短信 联系人输入时搜索提示显示列表
         *
         * @return
         */
        Observable<List<LinkmanBean>> findLinkmanAll();

        Observable<SmsBean> findLinkmanByPhoneNumber(String phoneNumber);
    }

    interface Presenter extends IPresenter{
        void findContactsAll();

        void findLinkmanAll();

        void findLinkmanByPhoneNumber(String phoneNumber);

    }

}
