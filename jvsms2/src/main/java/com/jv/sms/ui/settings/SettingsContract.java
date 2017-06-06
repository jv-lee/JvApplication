package com.jv.sms.ui.settings;

import com.jv.sms.base.mvp.IModel;
import com.jv.sms.base.mvp.IPresenter;
import com.jv.sms.base.mvp.IView;
import com.jv.sms.entity.SettingBean;
import com.jv.sms.entity.SmsAppBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface SettingsContract {
    interface View extends IView {
        void setSettingsBeans(List<SettingBean> settingsBeans);

        void isDefaultApplication(List<SmsAppBean> smsAppBeans);

        void notDefaultApplication();
    }

    interface Model extends IModel {
        Observable<List<SettingBean>> findSettingBeans();

        Observable<List<SmsAppBean>> hasDefaultSmsApplication();
    }

    interface Presenter extends IPresenter {
        void findSettingBeans();

        void clickDefaultSmsApplication();
    }
}
