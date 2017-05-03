package com.jv.sms.ui.settings;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jv.sms.R;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.mvp.BaseActivity;
import com.jv.sms.constant.Constant;
import com.jv.sms.entity.SettingBean;
import com.jv.sms.entity.SmsAppBean;
import com.jv.sms.rx.EventBase;
import com.jv.sms.ui.settings.adapter.SettingsAdapter;
import com.jv.sms.ui.settings.adapter.SettingsAlertAdapter;
import com.jv.sms.ui.settings.inject.DaggerSettingsComponent;
import com.jv.sms.ui.settings.inject.SettingsModule;
import com.jv.sms.utils.SPUtil;
import com.jv.sms.utils.SmsUtil;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import swipebacklayout.SwipeBackLayout;

/**
 * Created by Administrator on 2017/4/28.
 */

public class SettingsActivity extends BaseActivity<SettingsContract.Presenter>
        implements SettingsContract.View, SettingsAdapter.OnItemClick, SettingsAlertAdapter.AlertInterface {

    @BindView(R.id.lv_item_container)
    RecyclerView mItemContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AlertDialog defaultAlert;

    private List<SettingBean> settingBeans;
    private SettingsAdapter adapter;
    private SettingsAlertAdapter alertAdapter;

    @Override
    protected int bindRootView() {
        return R.layout.activity_settings;
    }

    @Override
    protected void bindData() {
        mItemContainer.setLayoutManager(new LinearLayoutManager(this));

        mPresenter.findSettingBeans();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSettingsComponent
                .builder()
                .appComponent(appComponent)
                .settingsModule(new SettingsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void rxEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                finish();
            }
        });
    }

    @Override
    public void setSettingsBeans(List<SettingBean> settingBeans) {

        if (this.settingBeans == null) {
            this.settingBeans = settingBeans;
            adapter = new SettingsAdapter(this, this, this.settingBeans);
            mItemContainer.setAdapter(adapter);
        } else {
            this.settingBeans.clear();
            this.settingBeans.addAll(settingBeans);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void isDefaultApplication(List<SmsAppBean> smsAppBeans) {

        if (defaultAlert == null) {
            //创建弹窗View
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            alertAdapter = new SettingsAlertAdapter(this, this, smsAppBeans);
            recyclerView.setAdapter(alertAdapter);

            //构造弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            defaultAlert = builder.setTitle(mApplication.getString(R.string.update_default_sms_application))
                    .setView(recyclerView)
                    .setPositiveButton(mApplication.getString(R.string.str_dialog_no), null)
                    .create();
        } else {
            alertAdapter.clearData(smsAppBeans);
        }

        defaultAlert.show();

    }

    @Override
    public void notDefaultApplication() {
        SmsUtil.hasDefaultSmsApplicationStartSettings(this, 0x0025);
    }

    @Override
    public void itemClick(View view, int position) {
        SettingBean settingBean = settingBeans.get(position);
        switch (position) {
            case 0: {
                mPresenter.clickDefaultSmsApplication();
            }
            break;
            case 1:
                if (!adapter.defaultFlag) return;
                adapter.switchFlag = settingBean.isHasOp() == true ? false : true;
                itemClickMethod(settingBean, view, Constant.SETTINGS_NOTIFICATION, position);
                adapter.notifyItemChanged(3);
                adapter.notifyItemChanged(4);
                break;
            case 2:
                if (!adapter.switchFlag) return;
                break;
            case 3:
                if (!adapter.switchFlag || !adapter.defaultFlag) return;
                itemClickMethod(settingBean, view, Constant.SETTINGS_VOICE, position);
                break;
            case 4:
                if (!adapter.switchFlag || !adapter.defaultFlag) return;
                itemClickMethod(settingBean, view, Constant.SETTINGS_SHOCK, position);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    /**
     * 设置Switch 项点击
     *
     * @param settingBean
     * @param view
     * @param code
     */
    public void itemClickMethod(SettingBean settingBean, View view, String code, int position) {
        boolean hasOp = settingBean.isHasOp() == true ? false : true;
        SPUtil.save(code, hasOp);
        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.sc_settings_op);
        switchCompat.setChecked(hasOp);
        settingBeans.get(position).setHasOp(hasOp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.findSettingBeans();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dismissAlert() {
        defaultAlert.dismiss();
        mPresenter.findSettingBeans();
    }
}
