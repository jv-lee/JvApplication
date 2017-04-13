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
        this(context, "daily", null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table stories ( " +
                "id integer primary key, " +
                "title text, " +
                "type integer, " +
                "ga_prefix text, " +
                "multipic integer, " +
                "image text，" +
                "newsDate text ) ");

        db.execSQL("create table top_stories ( " +
                "id integer primary key, " +
                "title text, " +
                "type integer, " +
                "ga_prefix text, " +
                "image text，" +
                "newsDate text ) ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
