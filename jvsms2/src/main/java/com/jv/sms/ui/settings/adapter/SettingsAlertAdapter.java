package com.jv.sms.ui.settings.adapter;

import android.content.Context;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.entity.SmsAppBean;
import com.jv.sms.utils.SmsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/12.
 */

public class SettingsAlertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SmsAppBean> smsAppBeans;
    private Context context;
    private AlertInterface alertListener;

    public SettingsAlertAdapter(Context context, AlertInterface alertListener, List<SmsAppBean> smsAppBeans) {
        this.context = context;
        this.alertListener = alertListener;
        this.smsAppBeans = smsAppBeans;
    }

    public void clearData(List<SmsAppBean> smsAppBeans) {
        this.smsAppBeans.clear();
        this.smsAppBeans = smsAppBeans;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsAlertViewHolder(LayoutInflater.from(context).inflate(R.layout.item_settings_default_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingsAlertViewHolder) holder).bindItemData(smsAppBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return smsAppBeans == null ? 0 : smsAppBeans.size();
    }

    class SettingsAlertViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_item_layout)
        LinearLayout llItemLayout;
        @BindView(R.id.iv_item_icon)
        ImageView ivItemIcon;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;

        public SettingsAlertViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItemData(SmsAppBean bean) {
            ivItemIcon.setImageDrawable(bean.getDrawable());
            tvItemName.setText(bean.getName());
        }


        @OnClick(R.id.ll_item_layout)
        public void onLayoutClick(View view) {
            SmsAppBean bean = smsAppBeans.get(getLayoutPosition());
            if (Telephony.Sms.getDefaultSmsPackage(context).equals(bean.getPackageName())) {
                alertListener.dismissAlert();
            } else {
                SmsUtil.setDefaultSmsApplicationStartSettings(context, 0x0025, bean.getPackageName());
                alertListener.dismissAlert();
            }
        }

    }

    public interface AlertInterface {
        void dismissAlert();
    }

}
