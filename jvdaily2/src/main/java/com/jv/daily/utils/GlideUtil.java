package com.jv.daily.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.jv.daily.glide.GlideHelper;

/**
 * Created by Administrator on 2017/4/12.
 */

public class GlideUtil {

    public static void glideUrl(Context context, String url, View view) {
        GlideHelper.loadImage(url, (ImageView) view);
//        Glide.with(context)
//                .load(url)
//                .error(R.drawable.ic_launcher)
//                .placeholder(R.drawable.account_avatar)
//                .crossFade()
//                .into((ImageView) view);
    }

}
