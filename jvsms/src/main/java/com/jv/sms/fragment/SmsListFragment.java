package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.jv.sms.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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

    private List<SmsBean> mList;
    private SmsListDataAdapter mAdapter;

    private String title, thread_id, phoneNumber;

    private final String SENT_SMS_ACTION = "send_sms_action_code";

    public SmsListFragment() {
    }

    public SmsListFragment(ToolbarSetListener toolbarSetListener) {
        this.toolbarSetListener = toolbarSetListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getActivity().getIntent().getStringExtra("title");
        thread_id = getActivity().getIntent().getStringExtra("thread_id");
        phoneNumber = getActivity().getIntent().getStringExtra("phone_number");

        JvApplication.THIS_SMS_FRAGMENT_FLAG = phoneNumber;
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
        rvSmsListFragmentContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSmsListFragmentContainer.setItemAnimator(new DefaultItemAnimator());
        mPresenter.refreshSmsList(thread_id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return mAdapter.clearSelectMessageState();
        }
        return false;
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

    //RxBus事件监听函数
    @Override
    protected void rxEvent() {
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(EventBase eventBase) {
                        if (eventBase.getOption().equals(phoneNumber)) {
                            mAdapter.insertSmsListUi((SmsBean) eventBase.getObj());
                        }
                    }
                });
    }

    @Override
    public void refreshSmsList(List<SmsBean> list) {
        if (mList == null) {
            mList = list;
            mAdapter = new SmsListDataAdapter(getActivity(), mList, this);
            rvSmsListFragmentContainer.setAdapter(mAdapter);
            rvSmsListFragmentContainer.scrollToPosition(mAdapter.getItemCount() - 1);
        } else {
            mList.clear();
            mList = list;
            mAdapter.notifyDataSetChanged();
            rvSmsListFragmentContainer.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void showSmsListSuccess() {
        Toast.makeText(getActivity(), "load smsData Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSmsError() {
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
    public void sendSmsLoading(SmsBean smsBean) {
        //设置发送短信内容至显示
        RxBus.getInstance().post(new EventBase(smsBean.getPhoneNumber(), smsBean));
        etSmsContent.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarSetListener.setToolbarTitle(title);
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
            mPresenter.sendSms(sendPi, phoneNumber, content);
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
                break;
            case R.id.iv_window_delete:
                break;
            case R.id.iv_window_notification:
                Toast.makeText(getActivity(), "notification通知处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_add:
                Toast.makeText(getActivity(), "add添加处理", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}