package com.jv.sms.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.jv.sms.bean.SmsBean;
import com.jv.sms.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */

public class SmsDaoImpl implements ISmsDao {

    private DBHelper db;

    public SmsDaoImpl(Context context) {
        this.db = DBHelper.getInstance(context);
    }

    @Override
    public boolean save(String[] ids) {
        try {
            if (ids != null) {
                for (String bean : ids) {
                    db.getReadableDatabase().execSQL("insert into sms_notification values(null,?)", new Object[]{bean});
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String[] onIds) {
        try {
            if (onIds != null) {
                for (int i = 0; i < onIds.length; i++) {
                    db.getReadableDatabase().execSQL("delete from sms_notification where id = ?", new Object[]{onIds[i]});
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<SmsBean> findAll() {
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from sms_notification", null);

        List<SmsBean> smsList = new ArrayList<>();

        if (cursor != null) {

            while (cursor.moveToNext()) {
                smsList.add(new SmsBean(cursor.getInt(cursor.getColumnIndex("noid")), cursor.getString(cursor.getColumnIndex("id"))));
            }

        }
        cursor.close();

        return smsList;
    }
}
