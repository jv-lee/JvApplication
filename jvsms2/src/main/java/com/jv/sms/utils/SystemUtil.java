package com.jv.sms.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class SystemUtil {
    public static int getKeyboardHeight(Activity paramActivity) {

        int height = SystemUtil.getScreenHeight(paramActivity) - SystemUtil.getStatusBarHeight(paramActivity)
                - SystemUtil.getAppHeight(paramActivity);
        if (height == 0) {
            height = (int) SPUtil.get("KeyboardHeight", 787);//787为默认软键盘高度 基本差不离
        } else {
            SPUtil.save("KeyboardHeight", height);
        }
        return height;
    }

    /**
     * 屏幕分辨率高
     **/
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * statusBar高度
     **/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**
     * 可见屏幕高度
     **/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 关闭键盘
     **/
    public static void hideSoftInput(Context context, View paramEditText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    // below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return SystemUtil.getScreenHeight(paramActivity) - SystemUtil.getStatusBarHeight(paramActivity)
                - SystemUtil.getActionBarHeight(paramActivity) - SystemUtil.getKeyboardHeight(paramActivity);
    }

    /**
     * 获取actiobar高度
     **/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return SystemUtil.dip2px(paramActivity, 56);
        }
        int[] attrs = new int[]{android.R.attr.actionBarSize};
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, SystemUtil.dip2px(paramActivity, 56));
    }

    /**
     * dp转px
     **/
    public static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**
     * 键盘是否在显示
     **/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = SystemUtil.getScreenHeight(paramActivity) - SystemUtil.getStatusBarHeight(paramActivity)
                - SystemUtil.getAppHeight(paramActivity);
        return height != 0;
    }

    /**
     * 显示键盘
     **/
    public static void showKeyBoard(final Context context, final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }
}
