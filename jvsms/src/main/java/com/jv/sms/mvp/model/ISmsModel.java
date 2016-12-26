package com.jv.sms.mvp.model;

import android.content.Context;

import com.jv.sms.bean.SmsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ISmsModel {

    /**
     * 主界面显示当前所有会话第一条内容的集合
     *
     * @param context
     * @return
     */
    List<SmsBean> findSmsAll(Context context);

    /**
     * 主界面删除当前会话
     *
     * @param id
     * @return
     */
    boolean removeSmsByThreadId(String id);

    /**
     * 发送新的短信后 或 接收到新的短信 后 获取最新的会话 增加至主界面 增对于以前没有的会话内容 新增会话项
     *
     * @return
     */
    SmsBean getNewSms();

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
