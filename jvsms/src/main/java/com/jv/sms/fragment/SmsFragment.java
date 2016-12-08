package com.jv.sms.fragment;


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
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.adapter.SmsDataAdapter;
import com.jv.sms.mvp.presenter.ISmsPresenter;
import com.jv.sms.mvp.presenter.SmsPresenter;
import com.jv.sms.mvp.view.ISmsView;


import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsFragment extends Fragment implements ISmsView, SmsDataAdapter.OnSmsDataListener {

    private RecyclerView mRecyclerContainer;
    private ProgressBar mProgressBar;
    private ISmsPresenter mPresenter;
    private SmsDataAdapter mAdapter;
    private List<SmsBean> mList;

    public SmsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SmsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerContainer = (RecyclerView) view.findViewById(R.id.sms_fragment_recycler_container);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loadDataBar);
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
        Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDataSuccess() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "加载数据成功", Toast.LENGTH_SHORT).show();
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
    public void onIconClick(SmsBean bean) {
        Toast.makeText(getActivity(), "这是短信" + bean.getDate(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayoutClick(SmsBean bean) {
        Intent intent = new Intent(getActivity(), SmsListActivity.class);
        intent.putExtra("title", bean.getName());
        intent.putExtra("thread_id", bean.getThread_id());
        intent.putExtra("phone_number", bean.getPhoneNumber());
        startActivity(intent);
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
    }
}
