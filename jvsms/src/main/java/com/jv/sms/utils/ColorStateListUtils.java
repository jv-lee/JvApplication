package com.jv.sms.utils;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;

import com.jv.sms.app.JvApplication;

/**
 * Created by Administrator on 2017/1/13.
 */

public class ColorStateListUtils {

    public static ColorStateList getSwitchTrackColorStateList(int checkedColor, int noCheckedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = new int[]{android.R.attr.state_checked};
        colors[i] = ContextCompat.getColor(JvApplication.getInstance(), checkedColor);
        i++;

        states[i] = new int[0];
        colors[i] = ContextCompat.getColor(JvApplication.getInstance(), noCheckedColor);
        i++;

        return new ColorStateList(states, colors);
    }


}
