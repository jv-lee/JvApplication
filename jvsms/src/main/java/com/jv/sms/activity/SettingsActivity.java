package com.jv.sms.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.jv.sms.R;
import com.jv.sms.adapter.SettingsAdapter;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {


    @BindView(R.id.lv_item_container)
    ListView mItemContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void setThemes() {
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {


        PackageManager pm = getPackageManager();
        String name = "";
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(Telephony.Sms.getDefaultSmsPackage(this),
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SettingBean settingBean1 = new SettingBean("默认短信应用", name, false, false);
        SettingBean settingBean2 = new SettingBean("接受通知", "", true, true);
        SettingBean settingBean3 = new SettingBean("通知提示音", "默认铃声", false, false);
        SettingBean settingBean4 = new SettingBean("听到信息发送提示音", "", true, true);
        SettingBean settingBean5 = new SettingBean("震动", "", true, true);
        SettingBean settingBean6 = new SettingBean("您当前所在的国家/地区", "自动检测 (中国)", false, false);
        SettingBean settingBean7 = new SettingBean("当前手机号", "1325889965", false, false);
        List<SettingBean> settingBeans = new ArrayList<>();
        settingBeans.add(settingBean1);
        settingBeans.add(settingBean2);
        settingBeans.add(settingBean3);
        settingBeans.add(settingBean4);
        settingBeans.add(settingBean5);
        settingBeans.add(settingBean6);
        settingBeans.add(settingBean7);
        mItemContainer.setAdapter(new SettingsAdapter(this, settingBeans));
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


}
