package com.jv.sms.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.bean.SettingBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class SettingsAdapter extends BaseAdapter {

    private Context context;
    private List<SettingBean> settingBeans;

    public SettingsAdapter(Context context, List<SettingBean> settingBeans) {
        this.context = context;
        this.settingBeans = settingBeans;
    }

    @Override
    public int getCount() {
        return settingBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return settingBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_settings_title);
        TextView tvDes = (TextView) view.findViewById(R.id.tv_settings_des);
        SwitchCompat scOp = (SwitchCompat) view.findViewById(R.id.sc_settings_op);

        SettingBean setting = settingBeans.get(position);

        tvTitle.setText(setting.getTitle());

        if (setting.isSwitch()) {
            scOp.setVisibility(View.VISIBLE);
            tvDes.setVisibility(View.GONE);
            scOp.setChecked(setting.isHasOp());
        } else {
            scOp.setVisibility(View.GONE);
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText(setting.getDescription());
        }
        return view;
    }
}
