package com.jv.daily.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Administrator on 2017/8/15.
 */

public class GlideHelper {

    private volatile static GlideHelper mInstance = null;

    private static Context mContext = null;

    private static RequestOptions optionsCommand = null;
    private static DrawableTransitionOptions optionsDrawable = null;
    private static RequestOptions optionsCircleCrop = null;
    private static RequestOptions optionsCircleCropBg = null;
    private static RequestOptions optionsCircleCropYellowBg = null;

    private GlideHelper() {
        initOptions();
    }

    public static GlideHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GlideHelper.class) {
                if (mInstance == null) {
                    mContext = context;
                    mInstance = new GlideHelper();
                }
            }
        }
        return mInstance;
    }

    public static void loadImage(Object path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .transition(optionsDrawable)
                .apply(optionsCommand)
                .into(imageView);
    }

    public static void loadCircleCrop(Object path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .transition(optionsDrawable)
                .apply(optionsCircleCrop)
                .into(imageView);
    }

    public static void loadCircleCropBg(Object path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .transition(optionsDrawable)
                .apply(optionsCircleCropBg)
                .into(imageView);
    }

    public static void loadCircleCropyellowBg(Object path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .transition(optionsDrawable)
                .apply(optionsCircleCropYellowBg)
                .into(imageView);

    }

    private void initOptions() {
        //初始化普通加载
        if (optionsCommand == null) {
            optionsCommand = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
        }

        //初始化动画加载
        if (optionsDrawable == null) {
            optionsDrawable = new DrawableTransitionOptions();
            optionsDrawable.crossFade();
        }

        //初始化圆形图加载
        if (optionsCircleCrop == null) {
            optionsCircleCrop = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleTransform());
        }

        //初始化圆形图加载
        if (optionsCircleCrop == null) {
            optionsCircleCrop = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleTransform());
        }

        if (optionsCircleCropBg == null) {
            optionsCircleCropBg = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleTransform());
        }
        if (optionsCircleCropYellowBg == null) {
            optionsCircleCropYellowBg = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .transform(new GlideCircleYlTransform());
        }

    }



}
