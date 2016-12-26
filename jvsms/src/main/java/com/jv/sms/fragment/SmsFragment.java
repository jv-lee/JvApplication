package com.jv.sms.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.interfaces.DataLoadLayoutListener;
import com.jv.sms.adapter.SmsDataAdapter;
import com.jv.sms.base.BaseFragment;
import com.jv.sms.base.EventBase;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.presenter.ISmsPresenter;
import com.jv.sms.mvp.presenter.SmsPresenter;
import com.jv.sms.mvp.view.ISmsView;
import com.jv.sms.rx.RxBus;
import com.jv.sms.utils.NotificationUtils;
import com.jv.sms.utils.SizeUtils;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsFragment extends BaseFragment implements ISmsView, SmsDataAdapter.OnSmsDataListener, View.OnClickListener
        , SearchView.OnQueryTextListener {

    @BindView(R.id.rv_smsFragment_container)
    RecyclerView rvSmsFragmentContainer;
    private PopupWindow mPopupWindow;
    private View popupView;

    private SmsDataAdapter mAdapter;
    private List<SmsBean> mList;

    private DataLoadLayoutListener listener;

    private ISmsPresenter mPresenter;

    public SmsFragment() {
    }

    public SmsFragment(DataLoadLayoutListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SmsPresenter(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_sms;
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return RxBus.getInstance().register(this);
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        listener.showDataBar();
        initPopupView();

        if (listener.getSearchBar() != null) {
            listener.getSearchBar().setOnQueryTextListener(this);
        }

        rvSmsFragmentContainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvSmsFragmentContainer.setItemAnimator(new DefaultItemAnimator());
        mPresenter.findSmsAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            //判断当前是否选中 返回处理事件
            return mAdapter.clearSelectMessageState();
        }
        return true;
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
    }


    //RxBus事件监听函数
    @Override
    protected void rxEvent() {
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(final EventBase eventBase) {
                        //接受到删除通知 执行逻辑
                        if (((String) eventBase.getOption()).equals("deleteByThreadId")) {
                            mAdapter.deleteByThreadId((String) eventBase.getObj());
                        } else {
                            //添加短信通知执行逻辑
                            boolean hasSms = true;
                            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                                if (mAdapter.getItemBean(i).getPhoneNumber().equals(eventBase.getOption())) {
                                    hasSms = false;
                                    SmsBean smsBean = mAdapter.getItemBean(i);
                                    smsBean.setDate(((SmsBean) eventBase.getObj()).getDate());
                                    smsBean.setSmsBody(((SmsBean) eventBase.getObj()).getSmsBody());
                                    mAdapter.receiverSmsUpdate(i, smsBean);
                                    rvSmsFragmentContainer.scrollToPosition(0);
                                }
                            }
                            if (hasSms) {
                                mPresenter.getNewSms();
                            }
                        }
                    }
                });
    }

    @Override
    public void setData(List<SmsBean> beanList) {
        if (mList == null) {
            mList = beanList;
            mAdapter = new SmsDataAdapter(getActivity(), mList, this);
            rvSmsFragmentContainer.setAdapter(mAdapter);
        } else {
            mList.clear();
            mList = beanList;
            mAdapter = new SmsDataAdapter(getActivity(), mList, this);
            rvSmsFragmentContainer.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setDataError() {
        Toast.makeText(mContext, "数据加载错误或者为0", Toast.LENGTH_SHORT).show();
        listener.hideDataBar();
        listener.showDataLayout();
    }

    @Override
    public void setDataSuccess() {
        Toast.makeText(mContext, "数据加载成功", Toast.LENGTH_SHORT).show();
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
        mAdapter.receiverSmsAdd(sms);
        rvSmsFragmentContainer.scrollToPosition(0);
    }

    @Override
    public void insertSmsNotificationSuccess() {

    }

    @Override
    public void insertSmsNotificationError() {
    }

    @Override
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    @Override
    public void getInitPopupWindow() {
        initPopupWindow();
    }

    @Override
    public ISmsPresenter getPresenter() {
        return mPresenter;
    }


    /************************************************
     * 长按菜单
     **************************************************************/

    //创建弹窗布局设置点击监听
    private void initPopupView() {
        popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_select_window_menu, null);
        popupView.findViewById(R.id.iv_window_close).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_add).setOnClickListener(this);
        popupView.findViewById(R.id.iv_window_delete).setOnClickListener(this);
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
        mPopupWindow.showAtLocation(rvSmsFragmentContainer, Gravity.TOP, 0, SizeUtils.getSubTitleHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_close:
                mAdapter.closeWindowBtn();
                break;
            case R.id.iv_window_delete:
                mAdapter.deleteWindowBtn();
                break;
            case R.id.iv_window_add:
                Toast.makeText(getActivity(), "联系人App正在开发中", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.findByContent = query;
        mAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
