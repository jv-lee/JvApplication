package com.jv.sms.utils;

import android.content.ClipboardManager;
import android.content.Context;

import com.jv.sms.app.JvApplication;

/**
 * Created by Administrator on 2016/12/20.
 */

public class TextUtils {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) JvApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @return
     */
    public static String paste() {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) JvApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}
