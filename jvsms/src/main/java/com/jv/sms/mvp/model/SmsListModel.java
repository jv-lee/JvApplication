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
        ContentResolver cr = JvApplication.getInstance().getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read"};
        final Uri uri = Uri.parse("content://sms/");

        try {
            //获取当前短信对话游标对象
            Cursor cur = cr.query(Uri.parse("content://sms/"), projection, "thread_id=?", new String[]{threadId}, "date asc");
            //获取短信对象 添加至集合
            if (cur.moveToFirst()) {
                do {
                    smsList.add(SmsUtils.simpleSmsBean(cur));
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
