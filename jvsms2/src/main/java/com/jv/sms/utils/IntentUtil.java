package com.jv.sms.utils;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by Administrator on 2017/4/27.
 */

public class IntentUtil {

    public static void startActivityOrFinish(Activity context, Class<?> c) {
        context.startActivity(new Intent(context, c));
        context.finish();
    }
}
