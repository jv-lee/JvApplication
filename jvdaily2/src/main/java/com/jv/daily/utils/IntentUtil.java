package com.jv.daily.utils;

import android.content.Context;
import android.content.Intent;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/17.
 */

public class IntentUtil {

    public static void startActivity(Context context, Class<?> cls, Map<String, Object> args) {
        Intent intent = new Intent(context, cls);
        if (args != null) {
            Iterator it = args.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                intent.putExtra((String) entry.getKey(), (String) entry.getValue());
            }
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, String[][] args) {
        Intent intent = new Intent(context, cls);
        for (String[] strings : args) {
            intent.putExtra(strings[0], strings[1]);
        }
        context.startActivity(intent);
    }

}
