package com.jv.daily.db.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;
import com.jv.daily.db.DBHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/19.
 */
//@Singleton
public class NewsDao {

    private SQLiteDatabase db;

    public NewsDao(DBHelper dbHelper) {
        db = dbHelper.getReadableDatabase();

    }

    public boolean saveTopStories(List<TopStoriesBean> list, String date) {
        try {
            if (list != null && date != null) {
                for (TopStoriesBean bean : list) {
                    db.execSQL("insert into top_stories values(?,?,?,?,?,?)", new Object[]{bean.getId(), bean.getTitle(), bean.getType(), bean.getGa_prefix(), bean.getImage(), date});
                }
            } else {
                Log.d("dbError", " list && date -> null");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveStories(List<StoriesBean> list, String date) {
        try {
            if (list != null && date != null) {
                for (StoriesBean bean : list) {
                    db.execSQL("insert into stories(id,title,type,ga_prefix,multipic,image,date) values(?,?,?,?,?,?,?)", new Object[]{bean.getId(), bean.getTitle(), bean.getType(), bean.getGa_prefix(), bean.isMultipic() == true ? 0 : 1, bean.getImages().get(0), date});
                }
            } else {
                Log.d("dbError", " list && date -> null");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<StoriesBean> findStoriesAll(String date) {
        Cursor cursor = db.rawQuery("select * from stories where date = ?", new String[]{date});

        List<StoriesBean> list = new ArrayList<>();

        if (cursor != null) {

            while (cursor.moveToNext()) {
                StoriesBean storiesBean = new StoriesBean();
                storiesBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                storiesBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                storiesBean.setGa_prefix(cursor.getString(cursor.getColumnIndex("ga_prefix")));
                storiesBean.setMultipic(cursor.getInt(cursor.getColumnIndex("multipic")) == 0 ? true : false);
                storiesBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                List<String> images = new ArrayList<>();
                images.add(cursor.getString(cursor.getColumnIndex("image")));
                storiesBean.setImages(images);
                storiesBean.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(storiesBean);
            }

        }
        cursor.close();

        return list;
    }

    public List<TopStoriesBean> findTopStoriesAll(String date) {
        Cursor cursor = db.rawQuery("select * from top_stories where date = ?", new String[]{date});

        List<TopStoriesBean> list = new ArrayList<>();

        if (cursor != null) {

            while (cursor.moveToNext()) {
                TopStoriesBean storiesBean = new TopStoriesBean();
                storiesBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                storiesBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                storiesBean.setGa_prefix(cursor.getString(cursor.getColumnIndex("ga_prefix")));
                storiesBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                storiesBean.setImage(cursor.getString(cursor.getColumnIndex("image")));
                storiesBean.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(storiesBean);
            }

        }
        cursor.close();

        return list;
    }

    public int findTopStoriesCount(String date) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from top_stories  where date = ?", new String[]{date});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    count = (int) cursor.getLong(0);
                    Log.w("DAO", "count:" + count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("DAO", "exception - > " + e.getMessage());
        }
        if (cursor != null) {
            Log.w("DAO", "cursor.close()");
            cursor.close();
        }
        return count;
    }

    public int findStoriesCount(String date) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from stories  where date = ?", new String[]{date});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    count = (int) cursor.getLong(0);
                    Log.w("DAO", "count:" + count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("DAO", "exception - > " + e.getMessage());
        }
        if (cursor != null) {
            Log.w("DAO", "cursor.close()");
            cursor.close();
        }
        return count;
    }

    public List<TopStoriesBean> findAll(String date) {
        Cursor cursor = db.rawQuery("select * from top_stories", null);

        List<TopStoriesBean> list = new ArrayList<>();

        if (cursor != null) {

            while (cursor.moveToNext()) {
                TopStoriesBean storiesBean = new TopStoriesBean();
                storiesBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                storiesBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                storiesBean.setGa_prefix(cursor.getString(cursor.getColumnIndex("ga_prefix")));
                storiesBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                storiesBean.setImage(cursor.getString(cursor.getColumnIndex("image")));
                storiesBean.setDate(cursor.getString(cursor.getColumnIndex("dattt")));
                list.add(storiesBean);
            }

        }
        cursor.close();

        return list;
    }

}
