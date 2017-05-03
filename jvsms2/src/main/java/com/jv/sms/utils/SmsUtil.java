package com.jv.sms.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.jv.sms.entity.SmsBean;
import com.jv.sms.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsUtil {

    public static String getThisPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();
        if (phoneNumber.equals("") || phoneNumber == null) {
            phoneNumber = "获取号码失败";
        }
        return phoneNumber;
    }

    /**
     * 判断当前是否为默认短信应用
     *
     * @param context
     * @return
     */
    public static boolean hasDefaultsSmsApplication(Context context) {
        if (Telephony.Sms.getDefaultSmsPackage(context).equals(context.getPackageName())) {
            return true;
        }
        return false;
    }


    /**
     * 判断当前是否为默认短信应用 并启动设置
     *
     * @return
     */
    public static void hasDefaultSmsApplicationStartSettings(Context context, int request_code) {
        String packageName = context.getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(context).equals(packageName)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
            ((Activity) context).startActivityForResult(intent, request_code);
        }
    }

    /**
     * 使用packageName设置为 默认短信应用
     *
     * @param context
     * @param request_code
     * @param packageName
     */
    public static void setDefaultSmsApplicationStartSettings(Context context, int request_code, String packageName) {
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
        ((Activity) context).startActivityForResult(intent, request_code);
    }

    /**
     * 获取当前默认短信应用名称
     *
     * @return
     */
    public static String getDefaultSmsApplicationName(Context context) {

        PackageManager pm = context.getPackageManager();
        String name = "";
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(Telephony.Sms.getDefaultSmsPackage(context),
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 判断当前应用是否获得短信权限
     *
     * @param view
     * @param context
     */
    public static boolean setDefaultSms(View view, final Context context) {
        final String packageName = context.getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(context).equals(packageName)) {
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

    public static SmsBean findSmsByDate(Context context, long time) {
        SmsBean smsBean = null;

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://sms/"), new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"},
                "date = ?", new String[]{time + ""}, null);

        if (cursor.moveToFirst()) {
            smsBean = SmsUtil.simpleSmsBean(cursor, context);
        }

        return smsBean;
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
    public static void addSmsToDB(Context context, String address, String content, long date, int read, int type, int status) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("read", read);//0为未读信息
        values.put("type", type);//1为收件箱信息
        values.put("address", address);
        values.put("body", content);
        values.put("status", status);
        context.getContentResolver().insert(Uri.parse("content://sms"), values);
    }

    public static void updateSmsToDB(Context context, String id) {
        ContentResolver cr = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put("status", Constant.SMS_STATUS_ERROR);

        cr.update(Uri.parse("content://sms/"), contentValues, "_id = ?", new String[]{id});
    }

    /**
     * 获取条短信内容并格式化 返回短信对象
     *
     * @param cur
     * @return
     */
    public static SmsBean simpleSmsBean(Cursor cur, Context context) {
        //获取短信各参数
        String id = cur.getString(cur.getColumnIndex("_id"));
        String phoneNumber = cur.getString(cur.getColumnIndex("address"));
        String smsBody = cur.getString(cur.getColumnIndex("body"));
        String name = SmsUtil.getPeopleNameFromPerson(phoneNumber, context);
        String thread_id = cur.getString(cur.getColumnIndex("thread_id"));


        //获取状态参数
        int typeId = cur.getInt(cur.getColumnIndex("type"));
        int read = cur.getInt(cur.getColumnIndex("read"));
        int status = cur.getInt(cur.getColumnIndex("status"));

        //获取短信时间进行格式化
        String date = TimeUtil.milliseconds2String(cur.getLong(cur.getColumnIndex("date")));

        //设置短信收发类型 读取状态
        SmsBean.Type type = typeId == 1 ? SmsBean.Type.RECEIVE : SmsBean.Type.SEND;
        SmsBean.ReadType readType = read == 0 ? SmsBean.ReadType.NOT_READ : SmsBean.ReadType.IS_READ;

        SmsBean smsBean = new SmsBean(id, name, phoneNumber, smsBody, date, type, thread_id, readType, status);
        return smsBean;
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
