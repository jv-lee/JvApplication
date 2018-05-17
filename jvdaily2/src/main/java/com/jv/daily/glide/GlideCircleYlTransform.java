package com.jv.daily.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/9/15.
 */

public class GlideCircleYlTransform extends BitmapTransformation {

    public GlideCircleYlTransform() {
        super();
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        //绘制边框
        Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(4);//画笔宽度为4px
        mBorderPaint.setColor(Color.parseColor("#FFC443"));//边框颜色
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setAntiAlias(true);
        float r = size / 2f;
        float r1=(size-2*4)/2f;
        canvas.drawCircle(r, r, r1, paint);
        canvas.drawCircle(r,r,r1,mBorderPaint);//画边框
        return result;
    }

    @Override
    public Bitmap transform(BitmapPool pool, Bitmap toTransform,
                            int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
