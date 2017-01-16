package com.jv.sms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jv.sms.R;
import com.jv.sms.adapter.SettingsAdapter;
import com.jv.sms.adapter.SettingsAlertAdapter;
import com.jv.sms.app.JvApplication;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;
import com.jv.sms.constant.Constant;
import com.jv.sms.mvp.presenter.ISettingsPresenter;
import com.jv.sms.mvp.presenter.SettingsPresenter;
import com.jv.sms.mvp.view.ISettingsView;
import com.jv.sms.utils.SPHelper;
import com.jv.sms.utils.SmsUtils;

import java.util.List;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity implements ISettingsView, SettingsAdapter.OnItemClick, SettingsAlertAdapter.AlertInterface {


    @BindView(R.id.lv_item_container)
    RecyclerView mItemContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AlertDialog defaultAlert;

    private List<SettingBean> settingBeans;
    private SettingsAdapter adapter;
    private SettingsAlertAdapter alertAdapter;

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

        mItemContainer.setLayoutManager(new LinearLayoutManager(this));

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
            defaultAlert = builder.setTitle(JvApplication.getInstance().getString(R.string.update_default_sms_application))
                    .setView(recyclerView)
                    .setPositiveButton(JvApplication.getInstance().getString(R.string.str_dialog_no), null)
                    .create();
        } else {
            alertAdapter.clearData(smsAppBeans);
        }

        defaultAlert.show();

    }

    @Override
    public void notDefaultApplication() {
        SmsUtils.hasDefaultSmsApplicationStartSettings(this, 0x0025);
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
        SPHelper.save(code, hasOp);
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