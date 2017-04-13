package com.jv.daily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/13.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        //将参数写死  打开数据库, 不开启工厂 , 版本为1
        this(context, "news", null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table stories(" +
                "id INTEGER primary key," +
                "title TEXT," +
                "type INTEGER," +
                "ga_prefix TEXT," +
                "multipic INTEGER," +
                "image TEXT，" +
                "date TEXT)");

        db.execSQL("create table top_stories(" +
                "id INTEGER primary key," +
                "title TEXT," +
                "type INTEGER," +
                "ga_prefix TEXT," +
                "image TEXT，" +
                "date TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
