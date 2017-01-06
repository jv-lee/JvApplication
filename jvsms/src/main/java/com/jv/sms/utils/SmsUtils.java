package com.jv.sms.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

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
     * 判断当前应用是否获得短信权限
     *
     * @param view
     * @param context
     */
    public static boolean setDefaultSms(View view, final Context context) {
        final String packageName = JvApplication.getInstance().getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(JvApplication.getInstance()).equals(packageName)) {
            Snackbar.make(view, "请将 Jv Messenger 设为默认的短信应用", Snackbar.LENGTH_LONG).setAction("更改", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
                    ((Activity) context).startActivityForResult(intent, 0x00025);
                }
            }).show();
            return false;
        }
        return true;
    }

    /**
     * 发送短信
     */
    public static void sendSms(PendingIntent sentPI, String phoneNumber, String Content) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, Content, sentPI, null);
    }

    /**
     * 添加到系统短信数据库
     */
    public static void addSmsToDB(Context context, String address, String content, long date, int read, int type) {
        ContentValues values = new ContentValues();
//        values.put("date", date);
        values.put("read", read);//0为未读信息
        values.put("type", type);//1为收件箱信息
        values.put("address", address);
        values.put("body", content);
        context.getContentResolver().insert(Uri.parse("content://sms"), values);
    }

    /**
     * 获取条短信内容并格式化 返回短信对象
     *
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
