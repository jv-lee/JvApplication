package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.interfaces.ToolbarSetListener;
import com.jv.sms.adapter.SmsListDataAdapter;
import com.jv.sms.app.JvApplication;
import com.jv.sms.base.BaseFragment;
import com.jv.sms.base.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.presenter.ISmsListPresenter;
import com.jv.sms.mvp.presenter.SmsListPresenter;
import com.jv.sms.mvp.view.ISmsListView;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.ClickUtils;
import com.jv.sms.utils.SizeUtils;
import com.jv.sms.utils.TelUtils;
import com.jv.sms.utils.TimeUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsListFragment extends BaseFragment implements ISmsListView, View.OnClickListener, SmsListDataAdapter.OnSmsListAdapterListener {

    @BindView(R.id.rv_smsListFragment_container)
    RecyclerView rvSmsListFragmentContainer;
    @BindView(R.id.iv_add_sms)
    ImageView ivAddSms;
    @BindView(R.id.et_sms_content)
    EditText etSmsContent;
    @BindView(R.id.iv_emoji_sms)
    ImageView ivEmojiSms;
    @BindView(R.id.iv_send_sms)
    ImageView ivSendSms;
    private PopupWindow mPopupWindow;
    private View popupView;

    private ToolbarSetListener toolbarSetListener;
    private ISmsListPresenter mPresenter;

    private LinkedList<SmsBean> mList;
    private SmsListDataAdapter mAdapter;

    private SmsBean bean;
    private boolean etFlag = true;


    private final String SENT_SMS_ACTION = "send_sms_action_code";

    public SmsListFragment() {
    }

    public SmsListFragment(ToolbarSetListener toolbarSetListener) {
        this.toolbarSetListener = toolbarSetListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bean = (SmsBean) getActivity().getIntent().getSerializableExtra("bean");

        JvApplication.THIS_SMS_FRAGMENT_FLAG = bean.getPhoneNumber();
        mPresenter = new SmsListPresenter(this);
        getActivity().registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_sms_list;
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return RxBus.getInstance().register(this);
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        initPopupView();
        ivAddSms.setColorFilter(ContextCompat.getColor(mContext, JvApplication.icon_theme_darkColors[bean.getColorPosition()]));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);//设置倒叙显示消息列表
        rvSmsListFragmentContainer.setLayoutManager(linearLayoutManager);
        rvSmsListFragmentContainer.setItemAnimator(new DefaultItemAnimator());

        mPresenter.refreshSmsList(bean.getThread_id());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call:
                TelUtils.sendTel1(mContext, bean.getPhoneNumber());
                break;
            case R.id.menu_item_delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("提示")
                        .setMessage("确认删除当前会话？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteSmsByThreadId(bean.getThread_id());
                            }
                        })
                        .create().show();
                break;
            case R.id.menu_item_addContacts:
                Toast.makeText(mContext, "添加联系人功能正在开发", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return mAdapter.clearSelectMessageState();
        }
        return true;
    }

    @OnClick({R.id.iv_add_sms, R.id.iv_emoji_sms, R.id.iv_send_sms})
    public void ivOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_send_sms:
                sendSms();
                break;
            case R.id.iv_add_sms:
                Toast.makeText(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_emoji_sms:
                Toast.makeText(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnTextChanged(R.id.et_sms_content)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (!etFlag) return;
            etFlag = false;
            ivSendSms.setColorFilter(ContextCompat.getColor(mContext, JvApplication.icon_theme_darkColors[bean.getColorPosition()]));
        } else {
            etFlag = true;
            ivSendSms.setColorFilter(ContextCompat.getColor(mContext, R.color.colorSmsEditTextIcon));
        }
    }

    //RxBus事件监听函数
    @Override
    protected void rxEvent() {
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(EventBase eventBase) {
                        if (eventBase.getOption().equals(bean.getPhoneNumber())) {
                            mAdapter.insertSmsListUi((SmsBean) eventBase.getObj());
                        }
                    }
                });
    }

    @Override
    public void refreshSmsList(LinkedList<SmsBean> list) {
        if (mList == null) {
            mList = list;
            mAdapter = new SmsListDataAdapter(getActivity(), mList, this);
            rvSmsListFragmentContainer.setAdapter(mAdapter);
        } else {
            mList.clear();
            mList = list;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSmsListSuccess() {
        toolbarSetListener.hideProgressBar();
        Toast.makeText(getActivity(), "load smsData Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSmsError() {
        toolbarSetListener.hideProgressBar();
        Toast.makeText(getActivity(), "load smsData Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteSmsSuccess() {
        Toast.makeText(getActivity(), "delete sms success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteSmsError() {
        Toast.makeText(getActivity(), "delete sms error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendSmsSuccess() {
        Toast.makeText(getActivity(), "send sms success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendSmsError() {
        Toast.makeText(getActivity(), "send sms error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteThreadSuccess() {
        getActivity().finish();
        RxBus.getInstance().post(new EventBase("deleteByThreadId", bean.getThread_id()));
    }

    @Override
    public void deleteThreadError() {
        Toast.makeText(mContext, "删除失败，请检查权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendSmsLoading(SmsBean smsBean) {
        //设置发送短信内容至显示
        RxBus.getInstance().post(new EventBase(smsBean.getPhoneNumber(), smsBean));
        etSmsContent.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarSetListener.setToolbarTitle(bean.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeUtils.clearTimeList();//清空列表时间差管理集合
        getActivity().unregisterReceiver(sendMessage);//注销广播
        JvApplication.THIS_SMS_FRAGMENT_FLAG = "";//将当前全局号码初始化
    }


    //发送短信状态回调广播
    BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == Activity.RESULT_OK) {
                mPresenter.sendSmsSuccess();
            } else {
                mPresenter.sendSmsError();
            }
        }
    };

    public void sendSms() {
        String content = etSmsContent.getText().toString();
        if (content.length() > 0) {
            Intent sendIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sendPi = PendingIntent.getBroadcast(getActivity(), 0, sendIntent, 0);
            mPresenter.sendSms(sendPi, bean.getPhoneNumber(), content);
            ClickUtils.sendMusic();
        }
    }

    @Override
    public RecyclerView getRvContainer() {
        return rvSmsListFragmentContainer;
    }

    @Override
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    @Override
    public void getPopupWindowInit() {
        initPopupWindow();
    }

    @Override
    public int getThemePosition() {
        return bean.getColorPosition();
    }

    @Override
    public ISmsListPresenter getPresenter() {
        return mPresenter;
    }

    /************************************************
     * 长按菜单
     **************************************************************/

    //创建弹窗布局设置点击监听
    private void initPopupView() {
        popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_select_window_menu2, null);
        popupView.findViewById(R.id.iv_window_close).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_attachment).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_forward).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_copy).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_info).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_delete).setOnClickListener(this);
    }

    public void initPopupWindow() {
        //创建弹窗
        mPopupWindow = new PopupWindow(popupView, Toolbar.LayoutParams.MATCH_PARENT, toolbarSetListener.getToolbarHeight());
        mPopupWindow.setAnimationStyle(R.style.popup_window_anim);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        mPopupWindow.setFocusable(false);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        mPopupWindow.setOutsideTouchable(false);
        // TODO：更新popupwindow的状态
        mPopupWindow.update();
        mPopupWindow.showAtLocation(rvSmsListFragmentContainer, Gravity.TOP, 0, SizeUtils.getSubTitleHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_close:
                mAdapter.clearSelectMessageState();
                break;
            case R.id.iv_window_attachment:
                Toast.makeText(mContext, "分享功能正在开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_forward:
                Toast.makeText(mContext, "转发功能正在开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_copy:
                mAdapter.windowCopy();
                break;
            case R.id.iv_window_info:
                mAdapter.windowInfo();
                break;
            case R.id.iv_window_delete:
                mAdapter.windowDelete();
                break;
        }
    }


}