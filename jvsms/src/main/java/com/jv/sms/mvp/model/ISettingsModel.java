package com.jv.sms.mvp.model;

import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public interface ISettingsModel {

    List<SettingBean> findSettingBeans();

    List<SmsAppBean> hasDefaultSmsApplication();

}
