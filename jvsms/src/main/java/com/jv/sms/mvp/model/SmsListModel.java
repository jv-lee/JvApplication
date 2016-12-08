package com.jv.sms.mvp.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.utils.SmsUtils;
import com.jv.sms.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @SuppressLint("LongLogTag")
    @Override
    public List<SmsBean> refreshSmsList(String threadId) {
        List<SmsBean> smsList = new ArrayList<>();

        try {
            ContentResolver cr = JvApplication.getInstance().getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type", "thread_id", "read"};

            //获取当前短信对话游标对象
            Cursor cur = cr.query(Uri.parse("content://sms/"), projection, "thread_id=?", new String[]{threadId}, "date asc");

            if (cur.moveToFirst()) {
                do {
                    String id = cur.getString(cur.getColumnIndex("_id"));
                    String phoneNumber = cur.getString(cur.getColumnIndex("address"));
                    String smsBody = cur.getString(cur.getColumnIndex("body"));
                    int typeId = cur.getInt(cur.getColumnIndex("type"));
                    String name = SmsUtils.getPeopleNameFromPerson(phoneNumber, JvApplication.getInstance());
                    String thread_id = cur.getString(cur.getColumnIndex("thread_id"));
                    int read = cur.getInt(cur.getColumnIndex("read"));

                    //获取短信时间进行格式化
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(cur.getColumnIndex("date"))));
                    String date = dateFormat.format(d);

                    //设置短信收发类型
                    SmsBean.Type type = null;
                    if (typeId == 1) { //接收的短信类型
                        type = SmsBean.Type.RECEIVE;
                    } else if (typeId == 2) { //发送的短信类型
                        type = SmsBean.Type.SEND;
                    }

                    //设置读取状态
                    SmsBean.ReadType readType = null;
                    if (read == 0) {
                        readType = SmsBean.ReadType.notRead;
                    } else if (read == 1) {
                        readType = SmsBean.ReadType.isRead;
                    }

                    SmsBean sms = new SmsBean(id, name, phoneNumber, smsBody, date, type, thread_id, readType);
                    smsList.add(sms);

                    if (smsBody == null) smsBody = "";
                } while (cur.moveToNext());
            }

            //设置时间显示格式
            for (SmsBean sms : smsList) {
                sms.setShowDate(TimeUtils.isShowTime(sms.getDate()));
            }

            return smsList;
        } catch (SQLiteException ex) {
            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return null;
    }


}
