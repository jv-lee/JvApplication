package com.jv.sms.mvp.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class NewSmsModel implements INewSmsModel {


    @Override
    public List<SmsBean> findContactsAll() {
        List<SmsBean> list = new ArrayList<>();

        //查询raw_contacts表获得联系人id
        ContentResolver resolver = JvApplication.getInstance().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //查询联系人数据
        Cursor cursor = resolver.query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            SmsBean smsBean = new SmsBean();
            smsBean.setPhoneNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ", ""));
            smsBean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            smsBean.setColorPosition(Integer.parseInt(smsBean.getPhoneNumber().substring(smsBean.getPhoneNumber().length() - 1)) == 9 ? 0 : Integer.parseInt(smsBean.getPhoneNumber().substring(smsBean.getPhoneNumber().length() - 1)));
            list.add(smsBean);
        }

        return list;
    }
}
