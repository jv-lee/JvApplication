package com.jv.sms.mvp.model;

import android.app.PendingIntent;

import com.jv.sms.bean.SmsBean;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISmsListModel {

    /**
     * 根据id 删除会话界面短信
     *
     * @param id
     * @return
     */
    boolean deleteSmsListById(String id);

    /**
     * 查询点击后会话thread_id 搜索出的短信会话列表
     *
     * @param thread_id
     * @return
     */
    LinkedList<SmsBean> refreshSmsList(String thread_id);

    /**
     * 发送短信 保存至系统contentProvider
     *
     * @param sentPI
     * @param phoneNumber
     * @param content
     * @return
     */
    SmsBean sendSms(PendingIntent sentPI, String phoneNumber, String content);

    /**
     * 根据会话ID thread_id 删除当前会话列表所有内容
     *
     * @param thread_id
     * @return
     */
    boolean removeSmsByThreadId(String thread_id);
}
