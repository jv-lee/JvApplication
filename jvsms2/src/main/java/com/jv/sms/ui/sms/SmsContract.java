package com.jv.sms.ui.sms;

import android.content.Context;

import com.jv.sms.base.mvp.IModel;
import com.jv.sms.base.mvp.IPresenter;
import com.jv.sms.base.mvp.IView;
import com.jv.sms.entity.SmsBean;

import java.util.LinkedList;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface SmsContract {

    interface View extends IView {
        void setData(LinkedList<SmsBean> beanList);

        void setDataError();

        void setDataSuccess();

        void removeDataError();

        void removeDataSuccess(int position);

        void setNewSms(SmsBean sms);

        void insertSmsNotificationSuccess();

        void insertSmsNotificationError();
    }

    interface Presenter extends IPresenter {
        void findSmsAll();

        void removeSmsByThreadId(String id, int position);

        void getNewSms();

        void updateSmsState(SmsBean smsBean);

        void insertSmsNotification(String[] ids);

        void deleteSmsNotification(String[] ids);
    }

    interface Model extends IModel {
        /**
         * 主界面显示当前所有会话第一条内容的集合
         *
         * @param context
         * @return
         */
        Observable<LinkedList<SmsBean>> findSmsAll(Context context);

        /**
         * 主界面删除当前会话
         *
         * @param id
         * @return
         */
        Observable<Boolean> removeSmsByThreadId(String id);

        /**
         * 发送新的短信后 或 接收到新的短信 后 获取最新的会话 增加至主界面 增对于以前没有的会话内容 新增会话项
         *
         * @return
         */
        Observable<SmsBean> getNewSms();

        /**
         * 更改短信会话读取状态
         *
         * @param smsBean
         */
        void updateSmsState(SmsBean smsBean);

        /**
         * 新增短信到系统数据库 ContentProvider中
         *
         * @param ids
         * @return
         */
        boolean insertSmsDB(String[] ids);


        boolean deleteSmsDB(String[] ids);
    }

}
