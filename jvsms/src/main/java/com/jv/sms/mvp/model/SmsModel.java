package com.jv.sms.mvp.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.constant.Constant;
import com.jv.sms.db.dao.ISmsDao;
import com.jv.sms.db.dao.SmsDaoImpl;
import com.jv.sms.utils.SmsUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsModel implements ISmsModel {

    private LinkedList<SmsBean> smsBeanList;
    private List<String> threads;

    @SuppressLint("LongLogTag")
    @Override
    public LinkedList<SmsBean> findSmsAll(final Context context) {
        //create return Container ，get all session id
        smsBeanList = new LinkedList<>();
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
        ContentResolver resolver = JvApplication.getInstance().getContentResolver();
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

    @Override
    public void updateSmsState(SmsBean smsBean) {

        final ContentResolver cr = JvApplication.getInstance().getContentResolver();
        final Uri uri = Uri.parse("content://sms/");

        ContentValues contentValues = new ContentValues();
        contentValues.put("read", Constant.SMS_STATUS_IS_READ);

        cr.update(uri, contentValues, "_id = ?", new String[]{smsBean.getId()});

    }

    @Override
    public boolean insertSmsDB(String[] ids) {
        ISmsDao dao = new SmsDaoImpl(JvApplication.getInstance());
        return dao.save(ids);
    }

    @Override
    public boolean deleteSmsDB(String[] ids) {
        ISmsDao dao = new SmsDaoImpl(JvApplication.getInstance());
        return dao.delete(ids);
    }

}
