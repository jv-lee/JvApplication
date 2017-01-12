package com.jv.sms.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jv.sms.R;
import com.jv.sms.adapter.SettingsAdapter;
import com.jv.sms.adapter.SettingsAlertAdapter;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;
import com.jv.sms.mvp.presenter.ISettingsPresenter;
import com.jv.sms.mvp.presenter.SettingsPresenter;
import com.jv.sms.mvp.view.ISettingsView;
import com.jv.sms.utils.SmsUtils;

import java.util.List;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity implements ISettingsView, AdapterView.OnItemClickListener, SettingsAlertAdapter.AlertInterface {


    @BindView(R.id.lv_item_container)
    ListView mItemContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AlertDialog defaultAlert;

    private List<SettingBean> settingBeans;
    private SettingsAdapter adapter;

    private ISettingsPresenter mPresenter;

    private int REQUEST_CODE = 0x025;

    @Override
    public int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void setThemes() {
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {

        mItemContainer.setOnItemClickListener(this);

        mPresenter = new SettingsPresenter(this);

        mPresenter.findSettingBeans();
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void setSettingsBeans(List<SettingBean> settingBeans) {

        if (this.settingBeans == null) {
            this.settingBeans = settingBeans;
            adapter = new SettingsAdapter(this, this.settingBeans);
            mItemContainer.setAdapter(adapter);
        } else {
            this.settingBeans.clear();
            this.settingBeans.addAll(settingBeans);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void isDefaultApplication(List<SmsAppBean> smsAppBeans) {
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SettingsAlertAdapter(this, this, smsAppBeans));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        defaultAlert = builder.setTitle("要更改短信应用吗？")
                .setView(recyclerView)
                .setPositiveButton("取消", null)
                .create();
        defaultAlert.show();

    }

    @Override
    public void notDefaultApplication() {
        SmsUtils.hasDefaultSmsApplicationStartSettings(this, 0x0025);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mPresenter.clickDefaultSmsApplication();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
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