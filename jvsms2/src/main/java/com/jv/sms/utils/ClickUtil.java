package com.jv.sms.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.jv.sms.R;

/**
 * Created by 64118 on 2016/12/12.
 */

public class ClickUtil {

    public static void sendMusic(Context context) {
        SoundPool sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        int music = sp.load(context, R.raw.message_send, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        sp.play(music, 1, 1, 0, 0, 1);
    }


    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
