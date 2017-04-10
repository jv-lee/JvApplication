package com.jv.daily.app;

import android.app.Application;
import android.content.Context;

import com.jv.daily.utils.SPUtil;


/**
 * Created by Administrator on 2017/2/24.
 */

public class MyApplication extends Application {

    private static Context mContext;
    public static boolean hasFist = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) {
            mContext = this;
        }
        SPUtil.getInstance(this);
//        SkinConfig.setCanChangeStatusColor(true);
    }

    public static Context getInstance() {
        return mContext;
    }
}
