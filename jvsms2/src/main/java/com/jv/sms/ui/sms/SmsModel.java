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
import com.jv.sms.Config;
import com.jv.sms.db.dao.SmsDaoImpl;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.utils.SmsUtil;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
    public Observable<LinkedList<SmsBean>> findSmsAll(final Context context) {
        return Observable.create(new ObservableOnSubscribe<LinkedList<SmsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedList<SmsBean>> e) throws Exception {
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

                e.onNext(smsBeanList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> removeSmsByThreadId(String id) {
        return Observable.just(id)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {
                        ContentResolver resolver = mApplication.getContentResolver();
                        Uri uri = Uri.parse("content://sms/conversations/" + s);
                        int result = resolver.delete(uri, null, null);
                        if (result > 0) {
                            return true;
                        }
                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SmsBean> getNewSms() {
        return Observable.create(new ObservableOnSubscribe<SmsBean>() {
            @Override
            public void subscribe(ObservableEmitter<SmsBean> e) throws Exception {

                final ContentResolver cr = mApplication.getContentResolver();
                final String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id", "read", "status"};
                final Uri uri = Uri.parse("content://sms/");

                Cursor cur = cr.query(uri, projection, null, null, "date desc limit 1");
                if (cur.moveToFirst()) {
                    e.onNext(SmsUtil.simpleSmsBean(cur, mApplication));
                    e.onComplete();
                }
                e.onNext(new SmsBean("12", "你好", "10077", "这是一条短信", "2016-12-12", SmsBean.Type.SEND, "2855", SmsBean.ReadType.IS_READ, -1));
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void updateSmsState(SmsBean smsBean) {

        final ContentResolver cr = mApplication.getContentResolver();
        final Uri uri = Uri.parse("content://sms/");

        ContentValues contentValues = new ContentValues();
        contentValues.put("read", Config.SMS_STATUS_IS_READ);

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
