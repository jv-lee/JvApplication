package com.jv.daily.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/2/23.
 */

public class BindingUtil {

    @BindingAdapter({"url", "error", "placeholder"})
    public static void loadImage(ImageView imageView, String url, Drawable error, Drawable placeholder) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(error)
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

}

