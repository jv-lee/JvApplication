package com.jv.sms.ui.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Telephony;

import com.jv.sms.R;
import com.jv.sms.base.mvp.BaseModel;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.constant.Constant;
import com.jv.sms.entity.SettingBean;
import com.jv.sms.entity.SmsAppBean;
import com.jv.sms.utils.SPUtil;
import com.jv.sms.utils.SmsUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
public class SettingsModel extends BaseModel implements SettingsContract.Model {

    boolean[] settingOps = {false, true, false, true, true, false, false};

    @Inject
    public SettingsModel() {
    }

    @Override
    public List<SettingBean> findSettingBeans() {

        List<SettingBean> settingBeans = new ArrayList<>();

        for (int i = 0; i < settingOps.length; i++) {
            settingBeans.add(getSettingBean(i));
        }
        return settingBeans;
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public List<SmsAppBean> hasDefaultSmsApplication() {
        List<SmsAppBean> smsAppBeans = new ArrayList<>();

        //是默认应用返回应用列表 提供选择
        if (Telephony.Sms.getDefaultSmsPackage(mApplication).equals(mApplication.getPackageName())) {
            PackageManager packageManager = mApplication.getPackageManager();
            Intent intent = new Intent();
            intent.setAction("android.provider.Telephony.SMS_DELIVER");
            List<ResolveInfo> resolveInfos = packageManager.queryBroadcastReceivers(intent, PackageManager.GET_INTENT_FILTERS);

            for (ResolveInfo res : resolveInfos) {
                smsAppBeans.add(new SmsAppBean(res.activityInfo.loadLabel(mApplication.getPackageManager()).toString(), res.activityInfo.packageName, res.activityInfo.loadIcon(mApplication.getPackageManager())));
            }
            return smsAppBeans;
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
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], SmsUtil.getDefaultSmsApplicationName(mApplication), false, false);
                break;
            case 1: //是否接受通知
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], "", true, (boolean) SPUtil.get(Constant.SETTINGS_NOTIFICATION, true));
                break;
            case 2: //通知提示音
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], "默认铃声", false, false);
                break;
            case 3: //听到信息发送提示音
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], "", true, (boolean) SPUtil.get(Constant.SETTINGS_VOICE, true));
                break;
            case 4: //震动
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], "", true, (boolean) SPUtil.get(Constant.SETTINGS_SHOCK, true));
                break;
            case 5: //当前所在的国家地址
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], "自动检测 (中国)", false, false);
                break;
            case 6: //当前手机号
                settingBean = new SettingBean(mApplication.getResources().getStringArray(R.array.settings)[i], SmsUtil.getThisPhoneNumber(mApplication), false, false);
                break;
        }
        return settingBean;
    }

}
