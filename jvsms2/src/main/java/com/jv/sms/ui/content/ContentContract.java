package com.jv.sms.ui.content;

import android.app.PendingIntent;

import com.jv.sms.base.mvp.IModel;
import com.jv.sms.base.mvp.IPresenter;
import com.jv.sms.base.mvp.IView;
import com.jv.sms.entity.SmsBean;

import java.util.LinkedList;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface ContentContract {

    interface View extends IView{
        void setSmsBeansAll(LinkedList<SmsBean> list);

        void showSmsListSuccess();

        void showSmsError();

        void deleteSmsSuccess();

        void deleteSmsError();

        void sendSmsLoading(SmsBean smsBean);

        void sendSmsSuccess(SmsBean smsBean);

        void sendSmsError(SmsBean smsBean);

        void deleteThreadSuccess();

        void deleteThreadError();

        void reSendSms(boolean flag, int position);
    }

    interface Presenter extends IPresenter{
        void findSmsBeansAll(String thread_id);

        void removeSmsById(String id);

        void sendSms(PendingIntent sentPI, String phoneNumber, String content, long time);

        void sendSmsSuccess(SmsBean smsBean);

        void sendSmsError(SmsBean smsBean);

        void deleteSmsByThreadId(String thread_id);

        void reSendSms(String id, int position); //通过id 删除原发送失败短信 重新发起当前短信 填充时间
    }

    interface Model extends IModel{
        /**
         * 根据id 删除会话界面短信
         *
         * @param id
         * @return
         */
        Observable<Boolean> deleteSmsListById(String id);

        /**
         * 查询点击后会话thread_id 搜索出的短信会话列表
         *
         * @param thread_id
         * @return
         */
        Observable<LinkedList<SmsBean>> findSmsBeansAll(String thread_id);

        /**
         * 发送短信 保存至系统contentProvider
         *
         * @param sentPI
         * @param phoneNumber
         * @param content
         * @return
         */
        Observable<SmsBean> sendSms(PendingIntent sentPI, String phoneNumber, String content, long time);

        /**
         * 根据会话ID thread_id 删除当前会话列表所有内容
         *
         * @param thread_id
         * @return
         */
        Observable<Boolean> removeSmsByThreadId(String thread_id);

        Observable<SmsBean> updateSmsStatus(SmsBean smsBean);
    }

}
