package com.jv.sms.mvp.view;

import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public interface ISettingsView {

    void setSettingsBeans(List<SettingBean> settingsBeans);

    void isDefaultApplication(List<SmsAppBean> smsAppBeans);

    void notDefaultApplication();

}
