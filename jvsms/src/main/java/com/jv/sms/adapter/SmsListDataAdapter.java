package com.jv.sms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.bean.SmsListUiFlagBean;
import com.jv.sms.utils.ConstUtils;
import com.jv.sms.utils.TimeUtils;

import java.util.List;
import java.util.regex.Pattern;

import static com.jv.sms.utils.TimeUtils.getChineseTimeString;
import static com.jv.sms.utils.TimeUtils.getChineseTimeString2;


/**
 * Created by 64118 on 2016/12/5.
 */

public class SmsListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SmsBean.Sms> mList;
    private Context mContext;
    public SmsListUiFlagBean smsListUiFlagBean;

    public SmsListDataAdapter(Context context, List<SmsBean.Sms> list) {
        mContext = context;
        mList = list;
        smsListUiFlagBean = new SmsListUiFlagBean(mList.size());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    //根据当前类型返回相应短信类型
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == SmsBean.Sms.Type.SEND.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_send_sms, parent, false);
            return new SmsListDataSendHolder(view);
        } else if (viewType == SmsBean.Sms.Type.RECEIVE.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_receive_sms, parent, false);
            return new SmsListDataReceiveHolder(view);
        }
        return null;
    }

    //设置收发短信布局Holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SmsBean.Sms.Type.SEND.ordinal()) {
            ((SmsListDataSendHolder) holder).setItemData(mList.get(position));
        } else if (getItemViewType(position) == SmsBean.Sms.Type.RECEIVE.ordinal()) {
            ((SmsListDataReceiveHolder) holder).setItemData(mList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType().ordinal();
    }

    /**
     * 发送短信Item ViewHolder
     */
    public class SmsListDataSendHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvInfo, tvDate2;

        public SmsListDataSendHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvDate = (TextView) itemView.findViewById(R.id.send_msg_date);
            tvInfo = (TextView) itemView.findViewById(R.id.send_message_info);
            tvDate2 = (TextView) itemView.findViewById(R.id.send_message_date2);
            tvInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (clearSelectMessageState()) {
                        notifyDataSetChanged();
                        return;
                    }

                    if (smsListUiFlagBean.hasDateUi.get(getLayoutPosition())) {
                        smsListUiFlagBean.hasDateUi.set(getLayoutPosition(), false);
                        notifyDataSetChanged();
                    } else {
                        smsListUiFlagBean.hasDateUi.set(getLayoutPosition(), true);
                        notifyDataSetChanged();
                    }
                }
            });
            tvInfo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    smsListUiFlagBean.initHasMessageUi();
                    smsListUiFlagBean.hasMessageUi.set(getLayoutPosition(), false);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        public void setItemData(SmsBean.Sms bean) {
            if (bean.getShowDate() == true) {
                tvDate.setText(getChineseTimeString(bean.getDate()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                tvDate.setVisibility(View.GONE);
            }

            if (smsListUiFlagBean.hasMessageUi.get(getLayoutPosition())) {
                tvInfo.setBackgroundResource(R.drawable.shape_message_white);
                tvInfo.setTextColor(mContext.getResources().getColor(android.R.color.black));
            } else {
                tvInfo.setBackgroundResource(R.drawable.shape_message_blue_dark);
                tvInfo.setTextColor(mContext.getResources().getColor(android.R.color.white));
            }

            if (smsListUiFlagBean.hasDateUi.get(getLayoutPosition())) {
                tvDate2.setVisibility(View.GONE);
            } else {
                tvDate2.setVisibility(View.VISIBLE);
            }

            tvDate2.setText(getChineseTimeString(bean.getDate()));
            tvInfo.setText(bean.getSmsBody());
        }
    }

    /**
     * 接受短信item ViewHolder
     */
    public class SmsListDataReceiveHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvInfo, tvIcon, tvDate2;
        private ImageView ivIcon;
        private FrameLayout flIcon;

        public SmsListDataReceiveHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvDate = (TextView) itemView.findViewById(R.id.receive_msg_date);
            tvDate2 = (TextView) itemView.findViewById(R.id.receive_message_date2);
            tvInfo = (TextView) itemView.findViewById(R.id.receive_message_info);
            tvIcon = (TextView) itemView.findViewById(R.id.receive_textIcon);
            ivIcon = (ImageView) itemView.findViewById(R.id.receive_imgIcon);
            flIcon = (FrameLayout) itemView.findViewById(R.id.item_sms_icon_layout);

            //点击短信内容
            tvInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //拦截事件 判断当前是否有被选中的消息 做状态重置处理
                    if (clearSelectMessageState()) {
                        notifyDataSetChanged();
                        return;
                    }

                    //改变短信时间显示状态
                    if (smsListUiFlagBean.hasDateUi.get(getLayoutPosition())) {
                        smsListUiFlagBean.hasDateUi.set(getLayoutPosition(), false);
                        notifyDataSetChanged();
                    } else {
                        smsListUiFlagBean.hasDateUi.set(getLayoutPosition(), true);
                        notifyDataSetChanged();
                    }
                }
            });

            //短信内容 长按改变显示状态 -> 选中状态 刷新显示
            tvInfo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    smsListUiFlagBean.initHasMessageUi();
                    smsListUiFlagBean.hasMessageUi.set(getLayoutPosition(), false);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        public void setItemData(SmsBean.Sms bean) {
            //设置短信组 是否显示时间头
            if (bean.getShowDate() == true) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(getChineseTimeString(bean.getDate()));
            } else {
                tvDate.setVisibility(View.GONE);
            }

            //设置短信内容 显示状态
            if (smsListUiFlagBean.hasMessageUi.get(getLayoutPosition())) {
                tvInfo.setBackgroundResource(R.drawable.shape_message_blue);
            } else {
                tvInfo.setBackgroundResource(R.drawable.shape_message_blue_dark);
            }

            //设置短信时间 显示状态
            if (smsListUiFlagBean.hasDateUi.get(getLayoutPosition())) {
                tvDate2.setVisibility(View.GONE);
            } else {
                tvDate2.setVisibility(View.VISIBLE);
            }

            //设置头像内容
            String name = bean.getName();
            if (Pattern.compile("[0-9]*").matcher(name).matches()) {
                ivIcon.setImageResource(R.drawable.ic_person_light);
            } else {
                tvIcon.setText(name.substring(0, 1));
            }

            //无其他条件设置数据
            tvDate2.setText(getChineseTimeString(bean.getDate()));
            tvInfo.setText(bean.getSmsBody());
        }
    }

    public boolean clearSelectMessageState() {
        if (smsListUiFlagBean.extendHasMessageUi()) {
            smsListUiFlagBean.initHasMessageUi();
            return true;
        }
        return false;
    }

}
