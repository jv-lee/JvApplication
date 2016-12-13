package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.DataLoadLayoutListener;
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.bean.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.adapter.SmsDataAdapter;
import com.jv.sms.mvp.presenter.ISmsPresenter;
import com.jv.sms.mvp.presenter.SmsPresenter;
import com.jv.sms.mvp.view.ISmsView;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.NotificationUtils;
import com.jv.sms.utils.SizeUtils;


import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsFragment extends Fragment implements ISmsView, SmsDataAdapter.OnSmsDataListener, View.OnClickListener {

    private RecyclerView mRecyclerContainer;
    private PopupWindow mPopupWindow;
    private View popupView;

    private SmsDataAdapter mAdapter;
    private List<SmsBean> mList;

    private DataLoadLayoutListener listener;

    private ISmsPresenter mPresenter;
    private Observable<EventBase> observable;

    public SmsFragment() {
    }

    public SmsFragment(DataLoadLayoutListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SmsPresenter(this);
        observable = RxBus.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        initView(view);
        rxEvent();
        return view;
    }

    private void initView(View view) {
        listener.showDataBar();
        mRecyclerContainer = (RecyclerView) view.findViewById(R.id.sms_fragment_recycler_container);
        initPopupView();
        //解决嵌套滑动缓慢问题
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerContainer.setLayoutManager(layoutManager);
        mRecyclerContainer.setHasFixedSize(true);
        mRecyclerContainer.setNestedScrollingEnabled(false);
        mRecyclerContainer.setItemAnimator(new DefaultItemAnimator());
        mPresenter.findSmsAll();
    }

    //RxBus事件监听函数
    private void rxEvent() {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(final EventBase eventBase) {
                        boolean hasSms = true;
                        for (int i = 0; i < mAdapter.getItemCount(); i++) {
                            if (mAdapter.getItemBean(i).getPhoneNumber().equals(eventBase.getOption())) {
                                hasSms = false;
                                SmsBean smsBean = mAdapter.getItemBean(i);
                                smsBean.setDate(((SmsBean) eventBase.getObj()).getDate());
                                smsBean.setSmsBody(((SmsBean) eventBase.getObj()).getSmsBody());
                                mAdapter.mList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.mList.add(0, smsBean);
                                mAdapter.notifyItemInserted(0);
                            }
                        }
                        if (hasSms) {
                            mPresenter.getNewSms();
                        }
                    }
                });
    }

    @Override
    public void setData(List<SmsBean> beanList) {
        if (mList == null) {
            mList = beanList;
            mAdapter = new SmsDataAdapter(getActivity(), mList, this);
            mRecyclerContainer.setAdapter(mAdapter);
        } else {
            mList.clear();
            mList = beanList;
            mRecyclerContainer.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setDataError() {
        listener.hideDataBar();
        listener.showDataLayout();
    }

    @Override
    public void setDataSuccess() {
        listener.hideDataBar();
        listener.hideDataLayout();
    }

    @Override
    public void removeDataError() {
        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeDataSuccess() {
        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNewSms(SmsBean sms) {
        mAdapter.mList.add(0, sms);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void onIconClick(SmsBean bean) {
        Toast.makeText(getActivity(), "这是短信" + bean.getDate(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayoutClick(SmsBean bean, int position) {
        Intent intent = new Intent(getActivity(), SmsListActivity.class);
        intent.putExtra("title", bean.getName());
        intent.putExtra("thread_id", bean.getThread_id());
        intent.putExtra("phone_number", bean.getPhoneNumber());
        startActivity(intent);
        updateReadState(bean, position);
    }

    @Override
    public void onLongLayoutClick(SmsBean bean, int position) {
//        mList.remove(bean);
//        mPresenter.removeSmsByThreadId(bean.getThread_id());
//        mAdapter.notifyItemRemoved(position);
//        listener.showSelectBar();
        initPopupWindow();
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationUtils.clearNum();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mList = null;
        mPresenter = null;
        RxBus.getInstance().unregister(this);
    }

    /**
     * 修改显示已读状态
     *
     * @param bean
     * @param position
     */
    public void updateReadState(SmsBean bean, int position) {
        bean.setReadType(SmsBean.ReadType.IS_READ);
        mAdapter.mList.set(position, bean);
        mAdapter.notifyItemChanged(position);
        mPresenter.updateSmsState(bean);
    }

    //创建弹窗布局设置点击监听
    private void initPopupView() {
        popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_select_window_menu, null);
        popupView.findViewById(R.id.iv_window_close).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_add).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_archive).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_delete).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_dnd).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_notification).setOnClickListener(this);
    }

    public void initPopupWindow() {
        //创建弹窗
        mPopupWindow = new PopupWindow(popupView, Toolbar.LayoutParams.MATCH_PARENT, listener.getToolbarHeight());
        mPopupWindow.setAnimationStyle(R.style.popup_window_anim);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        mPopupWindow.setFocusable(false);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        mPopupWindow.setOutsideTouchable(false);
        // TODO：更新popupwindow的状态
        mPopupWindow.update();
        mPopupWindow.showAtLocation(mRecyclerContainer, Gravity.TOP, 0, SizeUtils.getSubTitleHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_close:
                mPopupWindow.dismiss();
                break;
            case R.id.iv_window_archive:
                Toast.makeText(getActivity(), "archive归档处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_delete:
                Toast.makeText(getActivity(), "delete删除处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_notification:
                Toast.makeText(getActivity(), "notification通知处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_add:
                Toast.makeText(getActivity(), "add添加处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_window_dnd:
                Toast.makeText(getActivity(), "dnd频闭处理", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
