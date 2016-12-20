package com.jv.sms.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.bean.SmsListUiFlagBean;
import com.jv.sms.mvp.presenter.ISmsListPresenter;
import com.jv.sms.utils.TestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.jv.sms.utils.TimeUtils.getChineseTimeString;


/**
 * Created by 64118 on 2016/12/5.
 */

public class SmsListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LinkedList<SmsBean> mList;
    private Context mContext;
    private OnSmsListAdapterListener mListener;
    public SmsListUiFlagBean smsListUiFlagBean;
    private int hasSelecting = -1;

    public SmsListDataAdapter(Context context, LinkedList<SmsBean> list, OnSmsListAdapterListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
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
        if (viewType == SmsBean.Type.SEND.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_send_sms, parent, false);
            return new SmsListDataSendHolder(view);
        } else if (viewType == SmsBean.Type.RECEIVE.ordinal()) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_receive_sms, parent, false);
            return new SmsListDataReceiveHolder(view);
        }
        return null;
    }

    //设置收发短信布局Holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SmsBean.Type.SEND.ordinal()) {
            ((SmsListDataSendHolder) holder).setItemData(mList.get(position));
        } else if (getItemViewType(position) == SmsBean.Type.RECEIVE.ordinal()) {
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
        @BindView(R.id.send_msg_date)
        TextView tvDate;
        @BindView(R.id.send_message_info)
        TextView tvInfo;
        @BindView(R.id.send_message_date2)
        TextView tvDate2;

        public SmsListDataSendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.send_message_info)
        public void sendMessageInfoClick() {
            onClickMessageClick(getLayoutPosition());
        }

        @OnLongClick(R.id.send_message_info)
        public boolean sendMessageInfoLongClick() {
            onLongMessageClick(getLayoutPosition());
            return true;
        }

        /**
         * 设置项数据
         *
         * @param bean
         */
        public void setItemData(SmsBean bean) {
            //设置当前列表时间差显示
            if (bean.getShowDate() == true) {
                tvDate.setText(getChineseTimeString(bean.getDate()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                tvDate.setVisibility(View.GONE);
            }

            //设置当前选中背景显示状态
            if (smsListUiFlagBean.hasMessageUi.get(getLayoutPosition())) {
                tvInfo.setBackgroundResource(R.drawable.shape_message_white);
                tvInfo.setTextColor(mContext.getResources().getColor(android.R.color.black));
            } else {
                tvInfo.setBackgroundResource(R.drawable.shape_message_blue_dark);
                tvInfo.setTextColor(mContext.getResources().getColor(android.R.color.white));
            }

            //设置当前点击显示第二时间
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
        @BindView(R.id.receive_msg_date)
        TextView tvDate;
        @BindView(R.id.receive_textIcon)
        TextView tvIcon;
        @BindView(R.id.receive_imgIcon)
        ImageView ivIcon;
        @BindView(R.id.item_sms_icon_layout)
        FrameLayout flIcon;
        @BindView(R.id.receive_message_info)
        TextView tvInfo;
        @BindView(R.id.receive_message_date2)
        TextView tvDate2;

        public SmsListDataReceiveHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //点击短信内容
        @OnClick(R.id.receive_message_info)
        public void sendMessageInfoClick() {
            onClickMessageClick(getLayoutPosition());
        }

        //短信内容 长按改变显示状态 -> 选中状态 刷新显示
        @OnLongClick(R.id.receive_message_info)
        public boolean sendMessageInfoLongClick() {
            onLongMessageClick(getLayoutPosition());
            return true;
        }

        public void setItemData(SmsBean bean) {
            //设置短信组 是否显示时间头
            if (bean.getShowDate() == true) {
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(getChineseTimeString(bean.getDate()));
            } else {
                tvDate.setVisibility(View.GONE);
            }

            //设置短信内容 显示状态
            if (smsListUiFlagBean.hasMessageUi.get(getLayoutPosition())) {
                GradientDrawable grad = (GradientDrawable) tvInfo.getBackground();
                grad.setColor(ContextCompat.getColor(mContext, JvApplication.icon_theme_colors[mListener.getThemePosition()]));

            } else {
                GradientDrawable grad = (GradientDrawable) tvInfo.getBackground();
                grad.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
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
            //设置头像背景
            GradientDrawable myGrad = (GradientDrawable) flIcon.getBackground();
            myGrad.setColor(ContextCompat.getColor(mContext, JvApplication.icon_theme_colors[mListener.getThemePosition()]));

            //无其他条件设置数据
            tvDate2.setText(getChineseTimeString(bean.getDate()));
            tvInfo.setText(bean.getSmsBody());
        }
    }

    /**
     * 短信内容 点击处理
     *
     * @param position
     */
    public void onClickMessageClick(int position) {
        //拦截事件 判断当前是否有被选中的消息 做状态重置处理
        if (clearSelectMessageState()) {
            return;
        }

        //改变短信时间显示状态
        if (smsListUiFlagBean.hasDateUi.get(position)) {
            smsListUiFlagBean.hasDateUi.set(position, false);
            notifyDataSetChanged();
        } else {
            smsListUiFlagBean.hasDateUi.set(position, true);
            notifyDataSetChanged();
        }
    }

    /**
     * 短信内容长按选中处理
     *
     * @param position
     */
    public void onLongMessageClick(int position) {
        //保存选中下标位置
        hasSelecting = position;
        //长按初始化其他选中状态 更改当前长按项为选中状态 通知更新
        smsListUiFlagBean.initHasMessageUi();
        smsListUiFlagBean.hasMessageUi.set(position, false);
        notifyDataSetChanged();

        //判断当前菜单窗口状态 做显示操作
        if (mListener.getPopupWindow() == null) {
            mListener.getPopupWindowInit();
        } else {
            if (!mListener.getPopupWindow().isShowing()) {
                mListener.getPopupWindowInit();
            }
        }
    }

    /**
     * 新增短信至当前适配器
     *
     * @param smsBean
     */
    public void insertSmsListUi(SmsBean smsBean) {
        mList.addFirst(smsBean);
        smsListUiFlagBean.updateSize(1);
        notifyItemInserted(0);
        mListener.getRvContainer().scrollToPosition(0);
    }

    /**
     * 清空消息选中状态
     *
     * @return
     */
    public boolean clearSelectMessageState() {
        if (smsListUiFlagBean.extendHasMessageUi()) {
            //初始化选中状态
            smsListUiFlagBean.initHasMessageUi();
            hasSelecting = -1;

            //关闭选择菜单 更新显示状态
            mListener.getPopupWindow().dismiss();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void clearSelectMessageThis(int position) {
        smsListUiFlagBean.initHasMessageUi();
        mListener.getPopupWindow().dismiss();
        notifyItemChanged(position);
    }

    public void windowCopy() {
        int position = smsListUiFlagBean.getSelectMessageUiPosition();
        String content = mList.get(position).getSmsBody();
        TestUtils.copy(content);
        Toast.makeText(mContext, "已复制短信内容", Toast.LENGTH_SHORT).show();
        clearSelectMessageThis(position);
    }

    public void windowInfo() {
        int position = smsListUiFlagBean.getSelectMessageUiPosition();
        SmsBean bean = mList.get(position);
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(mContext);
        aBuilder.setTitle("信息详情")
                .setMessage("类型：文字信息\n发件人：" + bean.getName() + "\n接收时间：" + bean.getDate())
                .create().show();
        clearSelectMessageThis(position);
    }

    public void windowDelete() {
        int position = smsListUiFlagBean.getSelectMessageUiPosition();
        mListener.getPresenter().removeSmsById(mList.get(position).getId());
        smsListUiFlagBean.initHasMessageUi();
        mListener.getPopupWindow().dismiss();
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnSmsListAdapterListener {

        RecyclerView getRvContainer();

        PopupWindow getPopupWindow();

        void getPopupWindowInit();

        int getThemePosition();

        ISmsListPresenter getPresenter();

    }

}
