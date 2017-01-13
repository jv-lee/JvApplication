package com.jv.sms.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;
import com.jv.sms.utils.ColorStateListUtils;
import com.jv.sms.utils.SmsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/10.
 */

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SettingBean> settingBeans;
    private OnItemClick onItemClick;

    public boolean switchFlag = true;
    public boolean defaultFlag = true;

    public SettingsAdapter(Context context, OnItemClick onItemClick, List<SettingBean> settingBeans) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.settingBeans = settingBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingsViewHolder) holder).bindItemData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return settingBeans.size();
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_settings_title)
        TextView tvTitle;
        @BindView(R.id.tv_settings_des)
        TextView tvDes;
        @BindView(R.id.sc_settings_op)
        SwitchCompat scOp;


        public SettingsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.itemClick(v, getLayoutPosition());
                }
            });
        }

        public void bindItemData(int position) {
            SettingBean setting = settingBeans.get(position);
            tvTitle.setText(setting.getTitle());

            if (position == 0) {
                defaultFlag = SmsUtils.hasDefaultsSmsApplication(JvApplication.getInstance());
            }

            //获取通知设置Flag
            if (position == 1) {
                switchFlag = setting.isHasOp();
            }

            //当前设置项包含 switch 选项
            if (setting.isSwitch()) {
                scOp.setVisibility(View.VISIBLE);
                tvDes.setVisibility(View.GONE);
                scOp.setChecked(setting.isHasOp());

                if (position == 1) {
                    if (defaultFlag) {
                        scOp.setTrackTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_true_track, R.color.switch_false_track));
                        scOp.setThumbTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_true_thumb, R.color.switch_false_thumb));
                    } else {
                        scOp.setTrackTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_track_dark, R.color.switch_track_dark));
                        scOp.setThumbTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_thumb_dark, R.color.switch_thumb_dark));
                    }
                }

                //当前通知设置打开状态来设置 3 4 项锁定状态
                if (position == 3 || position == 4) {
                    if (switchFlag && defaultFlag) {
                        scOp.setTrackTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_true_track, R.color.switch_false_track));
                        scOp.setThumbTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_true_thumb, R.color.switch_false_thumb));
                    } else {
                        scOp.setTrackTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_track_dark, R.color.switch_track_dark));
                        scOp.setThumbTintList(ColorStateListUtils.getSwitchTrackColorStateList(R.color.switch_thumb_dark, R.color.switch_thumb_dark));
                    }
                }

                //当前设置项不包含 switch 选项
            } else {
                scOp.setVisibility(View.GONE);
                tvDes.setVisibility(View.VISIBLE);
                tvDes.setText(setting.getDescription());
            }
        }

    }

    public interface OnItemClick {
        void itemClick(View view, int position);
    }

}
