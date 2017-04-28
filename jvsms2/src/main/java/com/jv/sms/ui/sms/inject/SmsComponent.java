package com.jv.sms.ui.sms.inject;

import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.sms.SmsActivity;
import com.jv.sms.ui.sms.SmsFragment;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/28.
 */
@ActivityScope
@Component(modules = SmsModule.class, dependencies = AppComponent.class)
public interface SmsComponent {

    void inject(SmsActivity activity);

    void inject(SmsFragment fragment);
}
