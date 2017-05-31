package com.jv.sms.ui.content.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.Config;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.ui.content.ContentActivity;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/5.
 */

public class ForwardDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SmsBean> smsBeans;
    private Context context;
    private String text;

    public ForwardDialogAdapter(Context context, String text) {
        this.context = context;
        this.text = text;
        smsBeans = Config.smsBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_forward, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindItemDate(smsBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return smsBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_phoneNumber)
        TextView tvItemPhoneNumber;
        @BindView(R.id.fl_item_layout)
        FrameLayout flItemLayout;
        @BindView(R.id.iv_item_icon)
        ImageView ivItemIcon;
        @BindView(R.id.tv_item_text)
        TextView tvItemText;
        @BindView(R.id.ll_item_layout)
        LinearLayout llItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItemDate(final SmsBean smsBean) {

            String name = smsBean.getName();
            if (!Pattern.compile("[0-9]*").matcher(name).matches()) {
                //当前name 非名字 设置第一个字符显示
                tvItemText.setText(name.substring(0, 1));
                ivItemIcon.setImageDrawable(null);
                tvItemName.setText(name);
                tvItemPhoneNumber.setText(smsBean.getPhoneNumber());
            } else {
                //当前name为空 设置头像图片
                ivItemIcon.setImageResource(R.drawable.ic_person);
                tvItemText.setText("");
                tvItemName.setText(name);
                tvItemPhoneNumber.setVisibility(View.GONE);
            }
            //设置背景颜色
            flItemLayout.setBackgroundResource(R.drawable.shape_icon_bg);
            GradientDrawable grad = (GradientDrawable) flItemLayout.getBackground();
            grad.setColor(ContextCompat.getColor(context, Config.icon_theme_colors[smsBean.getColorPosition()]));

        }

        @OnClick(R.id.ll_item_layout)
        public void onClick(View view) {
            Config.text = text;
            Intent intent = new Intent(context, ContentActivity.class);
            intent.putExtra("bean", smsBeans.get(getLayoutPosition()));
            Config.themeId = smsBeans.get(getLayoutPosition()).getColorPosition();
            context.startActivity(intent);
        }


    }

}
