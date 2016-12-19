package com.jv.sms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DBHelper extends SQLiteOpenHelper{
    //单列模式数据库对象
    public volatile static DBHelper instance;

    private DBHelper(Context context) {
        //将参数写死  打开数据库, 不开启工厂 , 版本为1
        super(context, "news", null, 1);
    }

    /**
     * 获取单列模式对象
     */
    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table sms_notification(" +
                "noid integer primary key," + //广告id
                "id text," +
                "position text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
