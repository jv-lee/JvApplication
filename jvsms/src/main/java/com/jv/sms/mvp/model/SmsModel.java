package com.jv.sms.mvp.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.utils.SmsUtils;
import com.jv.sms.utils.SmsWriteOpUtil;
import com.jv.sms.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsModel implements ISmsModel {

    private List<SmsBean> smsBeanList;
    private List<String> threads;

    @SuppressLint("LongLogTag")
    @Override
    public List<SmsBean> findSmsAll(Context context) {
        smsBeanList = new ArrayList<>();

        final String SMS_URI_ALL = "content://sms/";

        //首先获取所有短信会话ID
        threads = SmsUtils.getThreadsId(context);

        try {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type", "thread_id", "read"};
            Uri uri = Uri.parse(SMS_URI_ALL);

            for (int i = 0; i < threads.size(); i++) {
                Cursor cur = cr.query(uri, projection, "thread_id=?", new String[]{threads.get(i)}, "date desc limit 1");

                if (cur.moveToFirst()) {

                    do {
                        String id = cur.getString(cur.getColumnIndex("_id"));
                        String phoneNumber = cur.getString(cur.getColumnIndex("address"));
                        String smsBody = cur.getString(cur.getColumnIndex("body"));
                        int typeId = cur.getInt(cur.getColumnIndex("type"));
                        String name = SmsUtils.getPeopleNameFromPerson(phoneNumber, context);
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
                        smsBeanList.add(sms);

                        if (smsBody == null) smsBody = "";
                    } while (false);
                }

                cur.close();
                cur = null;
            }

        } catch (SQLiteException ex) {
            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        return smsBeanList;
    }

    @Override
    public boolean removeSmsByThreadId(String id) {
        Context context = JvApplication.getInstance();

        if (SmsWriteOpUtil.isWriteEnabled(context)) {
            SmsWriteOpUtil.setWriteEnabled(context, true);
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/conversations/" + id);
        int result = resolver.delete(uri, null, null);
        if (result > 0) {
            return true;
        }
        return false;
    }

}
