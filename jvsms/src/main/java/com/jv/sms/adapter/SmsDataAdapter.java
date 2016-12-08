package com.jv.sms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.utils.TimeUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 64118 on 2016/12/4.
 */

public class SmsDataAdapter extends RecyclerView.Adapter<SmsDataAdapter.SmsDataHolder> {

    private Context mContext;
    private List<SmsBean> mList;
    private OnSmsDataListener mListener;

    public SmsDataAdapter(Context context, List<SmsBean> list, OnSmsDataListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @Override
    public SmsDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sms1, parent, false);
        return new SmsDataHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsDataHolder holder, int position) {
        holder.initData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SmsDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mIcon, mNumber, mMessage, mDate;
        private FrameLayout mIconLayout;
        private ImageView mIconImg;

        public SmsDataHolder(View itemView) {
            super(itemView);
            initView(itemView);
            initListener(itemView);
        }

        private void initView(View itemView) {
            mIcon = (TextView) itemView.findViewById(R.id.item_sms_textIcon);
            mNumber = (TextView) itemView.findViewById(R.id.item_sms_number);
            mMessage = (TextView) itemView.findViewById(R.id.item_sms_msg);
            mDate = (TextView) itemView.findViewById(R.id.item_sms_date);
            mIconLayout = (FrameLayout) itemView.findViewById(R.id.item_sms_icon_layout);
            mIconImg = (ImageView) itemView.findViewById(R.id.item_sms_imgIcon);
        }

        private void initListener(View itemView) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mIconLayout.setOnClickListener(this);
        }

        private void initData(SmsBean bean) {
            mMessage.setText(bean.getListSms().get(0).getSmsBody());
            mDate.setText(TimeUtils.getChineseTimeString2(bean.getListSms().get(bean.getListSms().size() - 1).getDate()));

            //判断当前name是否为数字
            String name = bean.getListSms().get(0).getName();
            if (!Pattern.compile("[0-9]*").matcher(name).matches()) {
                mIcon.setText(name.substring(0, 1));
            } else {
                mIconImg.setImageResource(R.drawable.ic_person_light);
            }
            mNumber.setText(name);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.item_sms_layout:
                    mListener.onLayoutClick(mList.get(getLayoutPosition()));
                    break;
                case R.id.item_sms_icon_layout:
                    mListener.onIconClick(mList.get(getLayoutPosition()).getListSms().get(mList.get(getLayoutPosition()).getListSms().size() - 1));
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.item_sms_layout:
                    mListener.onLongLayoutClick(mList.get(getLayoutPosition()),getLayoutPosition());
                    break;
            }
            return false;
        }
    }

    public interface OnSmsDataListener {

        void onIconClick(SmsBean.Sms bean);

        void onLayoutClick(SmsBean bean);

        void onLongLayoutClick(SmsBean bean,int position);

    }


}
