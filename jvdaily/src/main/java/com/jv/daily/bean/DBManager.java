package com.jv.daily.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class DBManager {
    private final static String dbName = "daily_db";
    private static DBManager mInstance;
    private DaoMaster.OpenHelper openHelper;
    private Context context;

    private StoriesBeanDao storiesBeanDao;
    private TopStoriesBeanDao topStoriesBeanDao;

    private DBManager() {
    }

    /**
     * 构造方法私有化
     *
     * @param context
     */
    private DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        storiesBeanDao = new DaoMaster(getWritableDatabase()).newSession().getStoriesBeanDao();
        topStoriesBeanDao = new DaoMaster(getWritableDatabase()).newSession().getTopStoriesBeanDao();
    }

    /**
     * 获取单例 数据库管理对象
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     *
     * @return
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return openHelper.getWritableDatabase();
    }

    /**
     * 插入一条Top数据
     *
     * @param bean
     */
    public void insertTopStories(TopStoriesBean bean) {
        topStoriesBeanDao.insert(bean);
    }

    /**
     * 插入Top数据集合
     *
     * @param beans
     */
    public void insertTopStories(List<TopStoriesBean> beans) {
        topStoriesBeanDao.insertInTx(beans);
    }

    /**
     * 插入一条Top数据
     *
     * @param bean
     */
    public void insertStories(StoriesBean bean) {
        storiesBeanDao.insert(bean);
    }

    /**
     * 插入Top数据集合
     *
     * @param beans
     */
    public void insertStories(List<StoriesBean> beans) {
        storiesBeanDao.insertInTx(beans);
    }

    /**
     * 查询指定天 的Top 数据集合
     *
     * @param date
     * @return
     */
    public List<TopStoriesBean> queryTopStories(String date) {
        List<TopStoriesBean> topStoriesBeans = topStoriesBeanDao.queryBuilder().where(TopStoriesBeanDao.Properties.Date.eq(date)).orderAsc(TopStoriesBeanDao.Properties.Id).list();
        return topStoriesBeans.size() == 0 ? null : topStoriesBeans;
    }

    /**
     * 查询指定 天 的新闻数据集合
     *
     * @param date
     * @return
     */
    public List<StoriesBean> queryStories(String date) {
        List<StoriesBean> storiesBeans = storiesBeanDao.queryBuilder().where(StoriesBeanDao.Properties.Date.eq(date)).orderAsc(StoriesBeanDao.Properties.Id).list();
        return storiesBeans.size() == 0 ? null : storiesBeans;
    }


//    /**
//     * 查询用户列表
//     *
//     * @return
//     */
//    public List<User> queryUserList() {
//        return getUserDao().queryBuilder().list();
//    }
//
//    /**
//     * 查询用户列表
//     *
//     * @param age
//     * @return
//     */
//    public List<User> queryUserList(int age) {
//        //查询条件 数据库中年龄等于 查询年龄 且 根据Age 按 Asc排序
//        return getUserDao().queryBuilder().where(UserDao.Properties.Age.gt(age)).orderAsc(UserDao.Properties.Age).list();
//    }
//
//    /**
//     * 查询用户列表 分页函数
//     *
//     * @param limit
//     * @param offset
//     * @return
//     */
//    public List<User> queryUserList(int limit, int offset) {
//        return getUserDao().queryBuilder().limit(limit).offset(offset).list();
//    }


    //    /**
//     * 删除一条数据
//     *
//     * @param user
//     */
//    public void deleteUser(User user) {
//        getUserDao().delete(user);
//    }
//
//    /**
//     * 更新一条用户数据
//     *
//     * @param user
//     */
//    public void updateUser(User user) {
//        getUserDao().update(user);
//    }

}
