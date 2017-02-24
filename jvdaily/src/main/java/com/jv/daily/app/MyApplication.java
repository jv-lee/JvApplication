package com.jv.daily.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/2/24.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) {
            mContext = this;
        }
    }

    public static Context getInstance() {
        return mContext;
    }
}
