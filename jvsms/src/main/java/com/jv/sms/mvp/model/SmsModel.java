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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsModel implements ISmsModel {

    private List<SmsBean> smsBeanList;
    private List<String> threads;

    @SuppressLint("LongLogTag")
    @Override
    public List<SmsBean> findSmsAll(final Context context) {
        //create return Container ，get all session id
        smsBeanList = new ArrayList<>();
        threads = SmsUtils.getThreadsId(context);

        final ContentResolver cr = context.getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read"};
        final Uri uri = Uri.parse("content://sms/");

        try {

            for (int i = 0; i < threads.size(); i++) {
                Cursor cur = cr.query(uri, projection, "thread_id=?", new String[]{threads.get(i)}, "date desc limit 1");

                if (cur.moveToFirst()) {
                    smsBeanList.add(SmsUtils.simpleSmsBean(cur));
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

    @Override
    public SmsBean getNewSms() {
        final ContentResolver cr = JvApplication.getInstance().getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read"};
        final Uri uri = Uri.parse("content://sms/");

        Cursor cur = cr.query(uri, projection, null, null, "date desc limit 1");
        if (cur.moveToFirst()) {
            return SmsUtils.simpleSmsBean(cur);
        }
        return null;
    }

}
