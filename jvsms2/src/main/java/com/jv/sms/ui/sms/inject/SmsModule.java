package com.jv.sms.ui.sms.inject;

import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.sms.SmsContract;
import com.jv.sms.ui.sms.SmsModel;
import com.jv.sms.ui.sms.SmsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/28.
 */
@Module
public class SmsModule {

    private SmsContract.View view;

    public SmsModule(SmsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SmsContract.Model provideModel(SmsModel smsModel) {
        return smsModel;
    }

    @ActivityScope
    @Provides
    SmsContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SmsContract.Presenter providePresenter(SmsPresenter smsPresenter) {
        return smsPresenter;
    }

}
