package com.jv.sms.mvp.model;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.constant.Constant;
import com.jv.sms.utils.SmsUtils;
import com.jv.sms.utils.TimeUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/5.
 */

public class SmsListModel implements ISmsListModel {

    @Override
    public boolean deleteSmsListById(String id) {
        ContentResolver resolver = JvApplication.getInstance().getContentResolver();
        int count = resolver.delete(Telephony.Sms.CONTENT_URI, "_id=" + id, null);
        if (count > 0) return true;
        return false;
    }

    @Override
    public boolean removeSmsByThreadId(String id) {
        ContentResolver resolver = JvApplication.getInstance().getContentResolver();
        Uri uri = Uri.parse("content://sms/conversations/" + id);
        int result = resolver.delete(uri, null, null);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @SuppressLint("LongLogTag")
    @Override
    public LinkedList<SmsBean> refreshSmsList(String threadId) {

        LinkedList<SmsBean> smsList = new LinkedList<>();
        ContentResolver cr = JvApplication.getInstance().getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read"};
        final Uri uri = Uri.parse("content://sms/");

        try {
            //获取当前短信对话游标对象
            Cursor cur = cr.query(Uri.parse("content://sms/"), projection, "thread_id=?", new String[]{threadId}, "date desc");
            //获取短信对象 添加至集合
            if (cur.moveToFirst()) {
                do {
                    smsList.add(SmsUtils.simpleSmsBean(cur));
                } while (cur.moveToNext());
            }
            //设置时间显示格式 从最小时间开始设置
            for (int i = (smsList.size() - 1); i >= 0; i--) {
                smsList.get(i).setShowDate(TimeUtils.isShowTime(smsList.get(i).getDate()));
            }
            TimeUtils.clearTimeList();

            return smsList;
        } catch (SQLiteException ex) {
            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return null;
    }

    @Override
    public SmsBean sendSms(PendingIntent sentPI, String phoneNumber, String content) {
        //设置时间
        long date = System.currentTimeMillis();
        String dateStr = TimeUtils.milliseconds2String(date);

        //发送短信
        SmsUtils.sendSms(sentPI, phoneNumber, content);

        //将发送短信保存至数据库
        SmsUtils.addSmsToDB(JvApplication.getInstance(), phoneNumber, content, date, Constant.SMS_STATUS_IS_READ, Constant.SMS_STATUS_SEND);

        //返回发送的短信对象
        SmsBean sms = new SmsBean();
        sms.setDate(dateStr);
        sms.setSmsBody(content);
        sms.setPhoneNumber(phoneNumber);
        sms.setName(SmsUtils.getPeopleNameFromPerson(phoneNumber, JvApplication.getInstance()));
        sms.setReadType(SmsBean.ReadType.IS_READ);
        sms.setType(SmsBean.Type.SEND);
        sms.setShowDate(TimeUtils.isShowTime(dateStr));
        return sms;

    }


}
