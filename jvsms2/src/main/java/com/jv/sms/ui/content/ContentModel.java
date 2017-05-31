package com.jv.sms.ui.content;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.jv.sms.base.mvp.BaseModel;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.Config;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.utils.SmsUtil;
import com.jv.sms.utils.TimeUtil;

import java.util.LinkedList;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/4/28.
 */
@ActivityScope
public class ContentModel extends BaseModel implements ContentContract.Model {

    @Inject
    public ContentModel() {
    }

    @Override
    public boolean deleteSmsListById(String id) {
        ContentResolver resolver = mApplication.getContentResolver();
        int count = resolver.delete(Telephony.Sms.CONTENT_URI, "_id=" + id, null);
        if (count > 0) return true;
        return false;
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

    @SuppressLint("LongLogTag")
    @Override
    public LinkedList<SmsBean> findSmsBeansAll(String threadId) {

        LinkedList<SmsBean> smsList = new LinkedList<>();
        ContentResolver cr = mApplication.getContentResolver();
        final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"};

        try {
            //获取当前短信对话游标对象
            Cursor cur = cr.query(Uri.parse("content://sms/"), projection, "thread_id=?", new String[]{threadId}, "date desc");
            //获取短信对象 添加至集合
            if (cur.moveToFirst()) {
                do {
                    smsList.add(SmsUtil.simpleSmsBean(cur, mApplication));
                } while (cur.moveToNext());
            }
            //设置时间显示格式 从最小时间开始设置
            for (int i = (smsList.size() - 1); i >= 0; i--) {
                smsList.get(i).setShowDate(TimeUtil.isShowTime(smsList.get(i).getDate()));
            }
            TimeUtil.clearTimeList();

            return smsList;
        } catch (SQLiteException ex) {
            Log.e("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return null;
    }

    @Override
    public SmsBean sendSms(PendingIntent sentPI, String phoneNumber, String content, long time) {

        SmsUtil.addSmsToDB(mApplication, phoneNumber, content, time, Config.SMS_STATUS_IS_READ, Config.SMS_STATUS_SEND, -1);

        SmsBean smsBean = SmsUtil.findSmsByDate(mApplication, time);
        smsBean.setShowDate(TimeUtil.isShowTime(smsBean.getDate()));
        TimeUtil.clearTimeList();

        //发送短信
        SmsUtil.sendSms(sentPI, phoneNumber, content);
        return smsBean;
    }

    public SmsBean updateSmsStatus(SmsBean smsBean) {
        //将发送短信保存至数据库
        SmsUtil.updateSmsToDB(mApplication, smsBean.getId());
        smsBean.setStatus(Config.SMS_STATUS_ERROR);
        return smsBean;
    }
}
