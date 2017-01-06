package com.jv.sms.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.bean.SmsUiFlagBean;
import com.jv.sms.mvp.presenter.ISmsPresenter;
import com.jv.sms.utils.TimeUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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

    public void setFilter(List<SmsBean> list) {
        mList = new LinkedList<>();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class SmsDataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_sms_textIcon)
        TextView itemSmsTextIcon;
        @BindView(R.id.item_sms_imgIcon)
        ImageView itemSmsImgIcon;
        @BindView(R.id.item_sms_icon_layout)
        FrameLayout itemSmsIconLayout;
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

        /**
         * 绑定数据方法
         *
         * @param bean
         */
        private void initData(SmsBean bean) {

            //设置短信时间
            itemSmsDate.setText(TimeUtils.getChineseTimeString2(bean.getDate()));

            //设置当前消息是否已读样式
            if (bean.getReadType().equals(SmsBean.ReadType.NOT_READ)) {
                itemSmsNumber.getPaint().setFakeBoldText(true);
                itemSmsMsg.getPaint().setFakeBoldText(true);
                itemSmsMsg.setMaxLines(3);
            } else {
                itemSmsMsg.getPaint().setFakeBoldText(false);
                itemSmsNumber.getPaint().setFakeBoldText(false);
                itemSmsMsg.setMaxLines(1);
            }

            //判断当前name是否为数字
            String name = bean.getName();
            if (smsUiFlagBean.hasIconUi.get(getLayoutPosition())) {
                if (!Pattern.compile("[0-9]*").matcher(name).matches()) {
                    //当前name 非名字 设置第一个字符显示
                    itemSmsTextIcon.setText(name.substring(0, 1));
                    itemSmsImgIcon.setImageDrawable(null);
                } else {
                    //当前name为空 设置头像图片
                    itemSmsImgIcon.setImageResource(R.mipmap.ic_person_light);
                    itemSmsTextIcon.setText("");
                }
                //设置背景颜色
                itemSmsIconLayout.setBackgroundResource(R.drawable.shape_icon_bg);
                GradientDrawable grad = (GradientDrawable) itemSmsIconLayout.getBackground();
                grad.setColor(ContextCompat.getColor(mContext, JvApplication.icon_theme_colors[bean.getColorPosition()]));

                //当前为选中状态设置选中layout
            } else if (!smsUiFlagBean.hasIconUi.get(getLayoutPosition())) {
                itemSmsIconLayout.setBackgroundResource(R.drawable.shape_icon_bg2);
                itemSmsImgIcon.setImageResource(R.mipmap.ic_checkmark_small_blue);
                itemSmsTextIcon.setText("");
            }

            //设置发件人姓名及 短信内容
            itemSmsNumber.setText(name);
            itemSmsMsg.setText(bean.getSmsBody());
            SpannableStringBuilder style;

            //设置短信内容 搜索过滤效果
            if (!bean.selectStr.equals("")) {

                //设置短信内容搜索效果
                if (bean.getSmsBody().toLowerCase().contains(bean.selectStr)) {
                    //过滤设置选中内容
                    int start = bean.getSmsBody().indexOf(bean.selectStr);
                    int end = start + bean.selectStr.length();
                    style = new SpannableStringBuilder(bean.getSmsBody());
                    style.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    itemSmsMsg.setText(style);
                }

                //设置短信内容过滤效果
                if (bean.getName().toLowerCase().contains(bean.selectStr)) {
                    int start = bean.getName().indexOf(bean.selectStr);
                    int end = start + bean.selectStr.length();
                    style = new SpannableStringBuilder(bean.getName());
                    style.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    itemSmsNumber.setText(style);
                }
            }


        }

        @OnClick({R.id.item_sms_layout, R.id.item_sms_icon_layout})
        public void onSmsLayoutClick(View view) {
            switch (view.getId()) {
                case R.id.item_sms_layout:
                    //项点击事件
                    layoutClick(mList.get(getLayoutPosition()), getLayoutPosition());
                    break;
                case R.id.item_sms_icon_layout:
                    //头像点击事件
                    iconClick(mList.get(getLayoutPosition()));
                    break;
            }
        }

        @OnLongClick(R.id.item_sms_layout)
        public boolean onSmsLayoutLongClick() {
            //项长按事件
            longLayoutClick(mList.get(getLayoutPosition()), getLayoutPosition());
            return true;
        }

    }

    public interface OnSmsDataListener {

        /**
         * 获取Fragment中Popup菜单
         *
         * @return
         */
        PopupWindow getPopupWindow();

        /**
         * 初始化Popup菜单 并显示
         */
        void getInitPopupWindow();

        /**
         * 获取presenter控制逻辑视图
         *
         * @return
         */
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

        //当前菜单为空则直接显示菜单
        if (mListener.getPopupWindow() == null) {
            mListener.getInitPopupWindow();
        } else {
            //当前菜单不为空 为不显示状态 则显示操作菜单
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
        intent.putExtra("bean", bean);
        JvApplication.themeId = bean.getColorPosition();
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

        //当前没有选择消息 则隐藏菜单
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
     * 清空消息选中状态
     *
     * @return
     */
    public boolean clearSelectMessageState() {
        if (smsUiFlagBean.extendHasMessageUi()) {
            closeWindowBtn();
            return true;
        }
        return false;
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("提示")
                .setMessage("确认删除选中会话？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWindowBtnMethod();
                    }
                })
                .create().show();

    }

    /**
     * popupWindow删除方法
     */
    public void deleteWindowBtnMethod() {
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

    public void deleteByThreadId(String thread_id) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getThread_id().equals(thread_id)) {
                mList.remove(i);
                smsUiFlagBean.hasIconUi.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

}
