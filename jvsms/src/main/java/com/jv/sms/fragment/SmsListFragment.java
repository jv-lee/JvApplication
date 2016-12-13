package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.ToolbarSetListener;
import com.jv.sms.adapter.SmsListDataAdapter;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.presenter.ISmsListPresenter;
import com.jv.sms.mvp.presenter.SmsListPresenter;
import com.jv.sms.mvp.view.ISmsListView;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.ClickUtils;
import com.jv.sms.utils.TimeUtils;


import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsListFragment extends Fragment implements ISmsListView, View.OnClickListener {

    private ToolbarSetListener toolbarSetListener;

    private ISmsListPresenter mPresenter;
    private Observable<EventBase> observable;

    private RecyclerView mRcvContainer;
    private ImageView mIvSendSms, mIvAdd, mIvEmoji;
    private EditText mEtSmsContent;

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
        observable = RxBus.getInstance().register(this);
        getActivity().registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_list, container, false);
        initView(view);
        rxEvent();
        return view;
    }


    private void initView(View view) {
        mRcvContainer = (RecyclerView) view.findViewById(R.id.rcv_container);
        mIvSendSms = (ImageView) view.findViewById(R.id.iv_send_sms);
        mEtSmsContent = (EditText) view.findViewById(R.id.et_sms_content);
        mIvAdd = (ImageView) view.findViewById(R.id.iv_add_sms);
        mIvEmoji = (ImageView) view.findViewById(R.id.iv_emoji_sms);
        mIvAdd.setOnClickListener(this);
        mIvEmoji.setOnClickListener(this);
        mIvSendSms.setOnClickListener(this);
        mRcvContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvContainer.setItemAnimator(new DefaultItemAnimator());
        mPresenter.refreshSmsList(thread_id);
    }

    //RxBus事件监听函数
    private void rxEvent() {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(EventBase eventBase) {
                        if (eventBase.getOption().equals(phoneNumber)) {
                            insertSmsListUi((SmsBean) eventBase.getObj());
                        }
                    }
                });
    }

    @Override
    public void refreshSmsList(List<SmsBean> list) {
        if (mList == null) {
            mList = list;
            mAdapter = new SmsListDataAdapter(getActivity(), mList);
            mRcvContainer.setAdapter(mAdapter);
            mRcvContainer.scrollToPosition(mAdapter.getItemCount() - 1);
        } else {
            mList.clear();
            mList = list;
            mAdapter.notifyDataSetChanged();
            mRcvContainer.scrollToPosition(mAdapter.getItemCount() - 1);
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
        mEtSmsContent.setText("");
    }

    public void insertSmsListUi(SmsBean smsBean) {
        mList.add(smsBean);
        mAdapter.smsListUiFlagBean.updateSize(1);
        mAdapter.notifyItemInserted(mAdapter.getItemCount());
        mRcvContainer.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarSetListener.setToolbarTitle(title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeUtils.clearTimeList();
        RxBus.getInstance().unregister(this);
        getActivity().unregisterReceiver(sendMessage);
        JvApplication.THIS_SMS_FRAGMENT_FLAG = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send_sms:
                String content = mEtSmsContent.getText().toString();
                if (content.length() > 0) {
                    Intent sendIntent = new Intent(SENT_SMS_ACTION);
                    PendingIntent sendPi = PendingIntent.getBroadcast(getActivity(), 0, sendIntent, 0);
                    mPresenter.sendSms(sendPi, phoneNumber, content);
                    ClickUtils.sendMusic();
                }
                break;
            case R.id.iv_add_sms:
                Toast.makeText(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_emoji_sms:
                Toast.makeText(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
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

}