package com.jv.sms.ui.sms;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BaseModel;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.constant.Constant;
import com.jv.sms.db.dao.SmsDaoImpl;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.utils.SmsUtil;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/4/28.
 */
@ActivityScope
public class SmsModel extends BaseModel implements SmsContract.Model {

    private LinkedList<SmsBean> smsBeanList;
    private List<String> threads;

    @Inject
    SmsDaoImpl dao;

    @Inject
    public SmsModel() {
    }

    @SuppressLint("LongLogTag")
    @Override
    public LinkedList<SmsBean> findSmsAll(final Context context) {
        //create return Container ，get all session id
        smsBeanList = new LinkedList<>();
        threads = SmsUtil.getThreadsId(context);

        final ContentResolver cr = context.getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"};
        final Uri uri = Uri.parse("content://sms/");

        try {

            for (int i = 0; i < threads.size(); i++) {
                Cursor cur = cr.query(uri, projection, "thread_id=?", new String[]{threads.get(i)}, "date desc limit 1");

                if (cur.moveToFirst()) {
                    smsBeanList.add(SmsUtil.simpleSmsBean(cur, mApplication));
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
        ContentResolver resolver = mApplication.getContentResolver();
        Uri uri = Uri.parse("content://sms/conversations/" + id);
        int result = resolver.delete(uri, null, null);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public SmsBean getNewSms() {
        final ContentResolver cr = mApplication.getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"};
        final Uri uri = Uri.parse("content://sms/");

        Cursor cur = cr.query(uri, projection, null, null, "date desc limit 1");
        if (cur.moveToFirst()) {
            return SmsUtil.simpleSmsBean(cur, mApplication);
        }
        Toast.makeText(mApplication, "Null", Toast.LENGTH_SHORT).show();
        return new SmsBean("12", "你好", "10077", "这是一条短信", "2016-12-12", SmsBean.Type.SEND, "2855", SmsBean.ReadType.IS_READ, -1);
    }

    @Override
    public void updateSmsState(SmsBean smsBean) {

        final ContentResolver cr = mApplication.getContentResolver();
        final Uri uri = Uri.parse("content://sms/");

        ContentValues contentValues = new ContentValues();
        contentValues.put("read", Constant.SMS_STATUS_IS_READ);

        cr.update(uri, contentValues, "_id = ?", new String[]{smsBean.getId()});

    }

    @Override
    public boolean insertSmsDB(String[] ids) {
        return dao.save(ids);
    }

    @Override
    public boolean deleteSmsDB(String[] ids) {
        return dao.delete(ids);
    }
}
