package com.jv.sms.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;


/**
 * Created by Administrator on 2017/1/13.
 */

public class ColorStateListUtil {

    public static ColorStateList getSwitchTrackColorStateList(Context context, int checkedColor, int noCheckedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = new int[]{android.R.attr.state_checked};
        colors[i] = ContextCompat.getColor(context, checkedColor);
        i++;

        states[i] = new int[0];
        colors[i] = ContextCompat.getColor(context, noCheckedColor);
        i++;

        return new ColorStateList(states, colors);
    }


}
