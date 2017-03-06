package com.jv.daily.app;

import android.content.Context;

import com.jv.daily.utils.SPUtil;

import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.base.SkinBaseApplication;

/**
 * Created by Administrator on 2017/2/24.
 */

public class MyApplication extends SkinBaseApplication {

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
