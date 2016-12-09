package com.jv.sms.utils;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsUtils {

    /**
     * 获取条短信内容并格式化 返回短信对象
     * @param cur
     * @return
     */
    public static SmsBean simpleSmsBean(Cursor cur) {
        //获取短信各参数
        String id = cur.getString(cur.getColumnIndex("_id"));
        String phoneNumber = cur.getString(cur.getColumnIndex("address"));
        String smsBody = cur.getString(cur.getColumnIndex("body"));
        String name = SmsUtils.getPeopleNameFromPerson(phoneNumber, JvApplication.getInstance());
        String thread_id = cur.getString(cur.getColumnIndex("thread_id"));

        //获取状态参数
        int typeId = cur.getInt(cur.getColumnIndex("type"));
        int read = cur.getInt(cur.getColumnIndex("read"));

        //获取短信时间进行格式化
        String date = TimeUtils.milliseconds2String(cur.getLong(cur.getColumnIndex("date")));

        //设置短信收发类型 读取状态
        SmsBean.Type type = typeId == 1 ? SmsBean.Type.RECEIVE : SmsBean.Type.SEND;
        SmsBean.ReadType readType = read == 0 ? SmsBean.ReadType.NOT_READ : SmsBean.ReadType.IS_READ;

        return new SmsBean(id, name, phoneNumber, smsBody, date, type, thread_id, readType);
    }

    /**
     * 获取当前手机所有短信会话ID
     *
     * @param context
     * @return
     */
    @SuppressLint("LongLogTag")
    public static List<String> getThreadsId(Context context) {
        List<String> threads = new ArrayList<>();

        final String SMS_URI_ALL = "content://sms/";

        try {
            //查询所有短信会话ID
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(Uri.parse(SMS_URI_ALL), new String[]{"thread_id"}, null, null, "date desc");
            if (cur.moveToFirst()) {
                do {
                    String thread_id = cur.getString(cur.getColumnIndex("thread_id"));
                    if (!threads.contains(thread_id)) {
                        threads.add(thread_id);
                    }
                } while (cur.moveToNext());
            }
            cur.close();
            cur = null;
        } catch (SQLiteException ex) {
            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return threads;
    }

    /**
     * 通过address手机号关联Contacts联系人的显示名字
     *
     * @param address
     * @return
     */
    public static String getPeopleNameFromPerson(String address, Context context) {
        if (address == null || address == "") {
            return null;
        }

        String strPerson;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        Uri uri_Person = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, address);  // address 手机号过滤
        Cursor cursor = context.getContentResolver().query(uri_Person, projection, null, null, null);

        if (cursor.moveToFirst()) {
            int index_PeopleName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String strPeopleName = cursor.getString(index_PeopleName);
            strPerson = strPeopleName;
        } else {
            strPerson = address;
        }
        cursor.close();
        cursor = null;
        return strPerson;
    }

}
