package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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


import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsFragment extends Fragment implements ISmsView, SmsDataAdapter.OnSmsDataListener {

    private RecyclerView mRecyclerContainer;
    private ISmsPresenter mPresenter;

    private SmsDataAdapter mAdapter;
    private List<SmsBean> mList;

    private DataLoadLayoutListener listener;
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
                            if (mList.get(i).getPhoneNumber().equals(eventBase.getOption())) {
                                hasSms = false;
                                SmsBean smsBean = mList.get(i);
                                smsBean.setDate(((SmsBean) eventBase.getObj()).getDate());
                                smsBean.setSmsBody(((SmsBean) eventBase.getObj()).getSmsBody());
                                mList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mList.add(0, smsBean);
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
        mList.add(0, sms);
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
        mList.remove(bean);
        mPresenter.removeSmsByThreadId(bean.getThread_id());
        mAdapter.notifyItemRemoved(position);
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
     * @param bean
     * @param position
     */
    public void updateReadState(SmsBean bean, int position) {
        bean.setReadType(SmsBean.ReadType.IS_READ);
        mList.set(position, bean);
        mAdapter.notifyItemChanged(position);
    }

}
