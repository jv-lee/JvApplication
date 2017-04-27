package com.jv.sms.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SmsAppBean {

    private String name;
    private String packageName;
    Drawable drawable;

    public SmsAppBean() {
    }

    public SmsAppBean(String name, String packageName, Drawable drawable) {
        this.name = name;
        this.packageName = packageName;
        this.drawable = drawable;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
