package com.jv.sms.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.bean.SmsUiFlagBean;
import com.jv.sms.mvp.presenter.ISmsPresenter;
import com.jv.sms.utils.TimeUtils;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by 64118 on 2016/12/4.
 */

public class SmsDataAdapter extends RecyclerView.Adapter<SmsDataAdapter.SmsDataHolder> {

    private Context mContext;
    public List<SmsBean> mList;
    private OnSmsDataListener mListener;
    public SmsUiFlagBean smsUiFlagBean;

    public SmsDataAdapter(Context context, List<SmsBean> list, OnSmsDataListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
        smsUiFlagBean = new SmsUiFlagBean(list.size());
    }

    public SmsBean getItemBean(int position) {
        return mList.get(position);
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

    class SmsDataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_sms_textIcon)
        TextView itemSmsTextIcon;
        @BindView(R.id.item_sms_imgIcon)
        ImageView itemSmsImgIcon;
        @BindView(R.id.item_sms_icon_layout)
        FrameLayout itemSmsIconLayout;
        @BindView(R.id.item_sms_notification)
        ImageView itemSmsNotification;
        @BindView(R.id.item_sms_number)
        TextView itemSmsNumber;
        @BindView(R.id.item_sms_msg)
        TextView itemSmsMsg;
        @BindView(R.id.item_sms_date)
        TextView itemSmsDate;
        @BindView(R.id.item_sms_layout)
        LinearLayout itemSmsLayout;


        public SmsDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void initData(SmsBean bean) {
            itemSmsMsg.setText(bean.getSmsBody());
            itemSmsDate.setText(TimeUtils.getChineseTimeString2(bean.getDate()));

            //设置当前消息是否已读样式
            if (bean.getReadType().equals(SmsBean.ReadType.NOT_READ)) {
                itemSmsNumber.getPaint().setFakeBoldText(true);
                itemSmsMsg.getPaint().setFakeBoldText(true);
            } else {
                itemSmsMsg.getPaint().setFakeBoldText(false);
                itemSmsNumber.getPaint().setFakeBoldText(false);
            }

            if (smsUiFlagBean.hasNotificationUi.get(getLayoutPosition())) {
                itemSmsNotification.setVisibility(View.GONE);
            } else {
                itemSmsNotification.setVisibility(View.VISIBLE);
            }

            //判断当前name是否为数字
            String name = bean.getName();
            if (smsUiFlagBean.hasIconUi.get(getLayoutPosition())) {
                if (!Pattern.compile("[0-9]*").matcher(name).matches()) {
                    itemSmsTextIcon.setText(name.substring(0, 1));
                    itemSmsImgIcon.setImageDrawable(null);
                    itemSmsIconLayout.setBackgroundResource(R.drawable.shape_icon_bg);
                } else {
                    itemSmsIconLayout.setBackgroundResource(R.drawable.shape_icon_bg);
                    itemSmsImgIcon.setImageResource(R.drawable.ic_person_light);
                    itemSmsTextIcon.setText("");
                }
            } else if (!smsUiFlagBean.hasIconUi.get(getLayoutPosition())) {
                itemSmsIconLayout.setBackgroundResource(R.drawable.shape_icon_bg2);
                itemSmsImgIcon.setImageResource(R.mipmap.ic_checkmark_small_blue);
                itemSmsTextIcon.setText("");
            }
            itemSmsNumber.setText(name);

        }

        @OnClick({R.id.item_sms_layout, R.id.item_sms_icon_layout})
        public void onSmsLayoutClick(View view) {
            switch (view.getId()) {
                case R.id.item_sms_layout:
                    layoutClick(mList.get(getLayoutPosition()), getLayoutPosition());
                    break;
                case R.id.item_sms_icon_layout:
                    iconClick(mList.get(getLayoutPosition()));
                    break;
            }
        }

        @OnLongClick(R.id.item_sms_layout)
        public boolean onSmsLayoutLongClick() {
            longLayoutClick(mList.get(getLayoutPosition()), getLayoutPosition());
            return true;
        }

    }

    public interface OnSmsDataListener {

        PopupWindow getPopupWindow();

        void getInitPopupWindow();

        ISmsPresenter getPresenter();

    }

    /**
     * 点击短信头像Click
     *
     * @param bean
     */
    public void iconClick(SmsBean bean) {
        Toast.makeText(mContext, "这是短信" + bean.getDate() + "Thread_id = " + bean.getThread_id(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长按显示Popup菜单
     *
     * @param bean
     * @param position
     */
    public void longLayoutClick(SmsBean bean, int position) {
        notifySelectUiFlag(position);

        //防止单选项清空时 继续走后续代码逻辑 导致重新显示菜单
        if (!smsUiFlagBean.extendHasMessageUi()) {
            return;
        }

        if (mListener.getPopupWindow() == null) {
            mListener.getInitPopupWindow();
        } else {
            if (!mListener.getPopupWindow().isShowing()) {
                mListener.getInitPopupWindow();
            }
        }
    }

    /**
     * 布局点击方法 判断Popup菜单未显示 则启动跳转，否则实现菜单选择逻辑更新Ui
     *
     * @param bean
     * @param position
     */
    public void layoutClick(SmsBean bean, int position) {
        if (mListener.getPopupWindow() != null) {
            if (!mListener.getPopupWindow().isShowing()) {
                startIntent(bean, position);
            } else {
                notifySelectUiFlag(position);
            }
        } else {
            startIntent(bean, position);
        }
    }

    /**
     * 点击跳转到相应短信界面 并修改已读状态
     *
     * @param bean
     * @param position
     */
    public void startIntent(SmsBean bean, int position) {
        Intent intent = new Intent(mContext, SmsListActivity.class);
        intent.putExtra("title", bean.getName());
        intent.putExtra("thread_id", bean.getThread_id());
        intent.putExtra("phone_number", bean.getPhoneNumber());
        mContext.startActivity(intent);
        updateReadState(bean, position);
    }

    /**
     * 更新多选Ui Flag 通过flase的 i= ？ :  来判定选中状态 最后通过判断当前UiFlag集合中全部为true 隐藏Ui显示
     *
     * @param position
     */
    public void notifySelectUiFlag(int position) {
        smsUiFlagBean.setFlag(position, false);
        notifyItemChanged(position);
        if (!smsUiFlagBean.extendHasMessageUi()) {
            mListener.getPopupWindow().dismiss();
            return;
        }
    }

    /**
     * 修改显示已读状态
     *
     * @param bean
     * @param position
     */
    public void updateReadState(SmsBean bean, int position) {
        bean.setReadType(SmsBean.ReadType.IS_READ);
        mList.set(position, bean);
        notifyItemChanged(position);
        mListener.getPresenter().updateSmsState(bean);
    }

    /**
     * 接受到新短信数据更改适配
     *
     * @param position
     * @param smsBean
     */
    public void receiverSmsUpdate(int position, SmsBean smsBean) {
        mList.remove(position);
        notifyItemRemoved(position);
        mList.add(0, smsBean);
        notifyItemInserted(0);
    }

    /**
     * 接收到新短信且为新的会话 更改适配器显示
     *
     * @param sms
     */
    public void receiverSmsAdd(SmsBean sms) {
        mList.add(0, sms);
        smsUiFlagBean.updateSize(1);
        notifyItemInserted(0);
    }

    /**
     * popupWindow 关闭按钮
     */
    public void closeWindowBtn() {
        int[] positionArray = smsUiFlagBean.getSelectPosition();
        smsUiFlagBean.initHasMessageUi();
        for (int i = 0; i < positionArray.length; i++) {
            notifyItemChanged(positionArray[i]);
        }
        mListener.getPopupWindow().dismiss();
    }

    /**
     * popupWindow 删除按钮
     */
    public void deleteWindowBtn() {

        int[] positionArray = smsUiFlagBean.getSelectPosition();
        for (int i = 0; i < positionArray.length; i++) {
            //数据操作必须放在第一位 ， 否则会出现下标排序错误
            Log.i("thread_id=", getItemBean(positionArray[i]).getThread_id());
            mListener.getPresenter().removeSmsByThreadId(getItemBean(positionArray[i]).getThread_id());
            mList.remove(positionArray[i]);
            smsUiFlagBean.hasIconUi.remove(positionArray[i]);
            notifyItemRemoved(positionArray[i]);
        }
        mListener.getPopupWindow().dismiss();

    }

    /**
     * popupWindow频闭通知按钮
     */
    public void notificationBtn() {
        int[] positionArray = smsUiFlagBean.getSelectPosition();
        String[] ids = new String[positionArray.length];
        for (int i = 0; i < positionArray.length; i++) {
            ids[i] = getItemBean(positionArray[i]).getId();
        }


    }


}
