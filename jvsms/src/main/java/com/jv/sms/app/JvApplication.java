package com.jv.sms.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/12/5.
 */

public class JvApplication extends Application {

    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
