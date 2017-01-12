package com.jv.sms.mvp.model;

import android.content.pm.PackageManager;
import android.provider.Telephony;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;
import com.jv.sms.utils.SmsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SettingsModel implements ISettingsModel {

    String[] settingTitles = {"默认短信应用", "接受通知", "通知提示音", "听到短信发送提示音", "震动", "您当前所在国家/地区", "当前手机号"};
    boolean[] settingOps = {false, true, false, true, true, false, false};

    @Override
    public List<SettingBean> findSettingBeans() {
        List<SettingBean> settingBeans = new ArrayList<>();

        for (int i = 0; i < settingOps.length; i++) {
            settingBeans.add(getSettingBean(i));
        }
        return settingBeans;
    }

    @Override
    public List<SmsAppBean> hasDefaultSmsApplication() {
        //是默认应用返回应用列表 提供选择
        if (Telephony.Sms.getDefaultSmsPackage(JvApplication.getInstance()).equals(JvApplication.getInstance().getPackageName())) {
            return null;
        } else { //非默认应用返回空 使其设置
            return null;
        }
    }

    /**
     * 获取当前设置数据实体类
     *
     * @param i
     * @return
     */
    private SettingBean getSettingBean(int i) {
        SettingBean settingBean = null;
        switch (i) {
            case 0: //当前默认短信应用
                settingBean = new SettingBean(settingTitles[i], SmsUtils.getDefaultSmsApplicationName(), false, false);
                break;
            case 1: //是否接受通知
                settingBean = new SettingBean(settingTitles[i], "", true, true);
                break;
            case 2: //通知提示音
                settingBean = new SettingBean(settingTitles[i], "默认铃声", false, false); //
                break;
            case 3: //听到信息发送提示音
                settingBean = new SettingBean(settingTitles[i], "", true, true);
                break;
            case 4: //震动
                settingBean = new SettingBean(settingTitles[i], "", true, true);
                break;
            case 5: //当前所在的国家地址
                settingBean = new SettingBean(settingTitles[i], "自动检测 (中国)", false, false);
                break;
            case 6: //当前手机号
                settingBean = new SettingBean(settingTitles[i], "1325889965", false, false);
                break;
        }
        return settingBean;
    }


}
