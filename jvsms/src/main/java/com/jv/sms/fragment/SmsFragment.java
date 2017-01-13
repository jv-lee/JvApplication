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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.SettingsActivity;
import com.jv.sms.app.JvApplication;
import com.jv.sms.constant.Constant;
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
import com.jv.sms.utils.SmsUtils;

import java.util.ArrayList;
import java.util.LinkedList;
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
        , SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    //数据填充容器 view
    @BindView(R.id.rv_container)
    RecyclerView rvContainer;

    //长按弹框 view
    private PopupWindow mPopupWindow;
    private View popupView;

    //搜索框view
    private SearchView mSearchView;

    //数据 及 适配器
    private SmsDataAdapter mAdapter;
    private LinkedList<SmsBean> mSmsBeans;

    // 加载数据 及 toolbar 监听接口回调
    private DataLoadLayoutListener listener;

    //presenter 控制层
    private ISmsPresenter mPresenter;

    public SmsFragment() {
    }

    public SmsFragment(DataLoadLayoutListener listener) {
        this.listener = listener;
    }

    /**
     * 清楚当前短信推送数量
     */
    @Override
    public void onResume() {
        super.onResume();
        NotificationUtils.clearNum();
    }

    /**
     * 释放引用数据
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSmsBeans = null;
        mPresenter = null;
    }

    /**
     * 初始化菜单 及 菜单搜索监听
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setQueryHint(JvApplication.getInstance().getString(R.string.search_message));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 菜单选择
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.menu_item_hideContacts:
                Toast.makeText(mContext, JvApplication.getInstance().getString(R.string.function_not), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Back键处理逻辑
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            //判断当前是否选中 返回处理事件
            if (mAdapter == null) return false;
            return mAdapter.clearSelectMessageState();
        }
        return true;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_sms;
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return RxBus.getInstance().register(this);
    }

    /**
     * 初始化 view函数
     *
     * @param savedInstanceState
     */
    @Override
    protected void initAllView(Bundle savedInstanceState) {
        initPresenter();
        if (listener != null) {
            listener.showDataBar(); //显示数据加载bar
        }

        initPopupView(); //初始化长按弹窗

        //创建显示容器 rv
        rvContainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvContainer.setItemAnimator(new DefaultItemAnimator());

        //控制层获取数据填充
        mPresenter.findSmsAll();
    }

    private void initPresenter() {
        mPresenter = new SmsPresenter(this);
    }

    /**
     * RxBus事件监听函数 监听通知 接受短信 新增短信
     */
    @Override
    protected void rxEvent() {
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventBase>() {
                    @Override
                    public void call(final EventBase eventBase) {
                        //接受到删除通知 执行逻辑
                        if (((String) eventBase.getOption()).equals(Constant.RX_CODE_DELETE_THREAD_ID)) {
                            mAdapter.deleteByThreadId((String) eventBase.getObj());
                        } else if (((String) eventBase.getOption()).equals(Constant.RX_CODE_UPDATE_MESSAGE)) {
                            mAdapter.updateShowMessage((SmsBean) eventBase.getObj());
                        } else {
                            //添加短信通知执行逻辑
                            boolean hasSms = true; //是否为存在短信
                            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                                if (mAdapter.getItemBean(i).getPhoneNumber().equals(eventBase.getOption())) {
                                    hasSms = false;
                                    SmsBean smsBean = mAdapter.getItemBean(i);
                                    smsBean.setDate(((SmsBean) eventBase.getObj()).getDate());
                                    smsBean.setSmsBody(((SmsBean) eventBase.getObj()).getSmsBody());
                                    mAdapter.receiverSmsUpdate(i, smsBean);
                                    rvContainer.scrollToPosition(0);
                                }
                            }
                            if (hasSms) {
                                if (mPresenter == null) initPresenter();
                                mPresenter.getNewSms();
                            }
                        }
                    }
                });
    }

    /**
     * 加载数据View 层回调
     *
     * @param beanList
     */
    @Override
    public void setData(LinkedList<SmsBean> beanList) {
        JvApplication.smsBeans = beanList; //保存当前临时数据
        if (mSmsBeans == null) {
            mSmsBeans = beanList == null ? new LinkedList<SmsBean>() : beanList;
            mAdapter = new SmsDataAdapter(getActivity(), mSmsBeans, this);
            rvContainer.setAdapter(mAdapter);
        } else {
            mSmsBeans.clear();
            mSmsBeans = beanList == null ? new LinkedList<SmsBean>() : beanList;
            mAdapter = new SmsDataAdapter(getActivity(), mSmsBeans, this);
            rvContainer.setAdapter(mAdapter);
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
        Toast.makeText(getActivity(), JvApplication.getInstance().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeDataSuccess(int position) {
        mAdapter.deleteWindowBtnMethodResult(position);
        if (mAdapter.getItemCount() == 0) {
            listener.showDataLayout();
        }
    }

    @Override
    public void setNewSms(SmsBean sms) {
        if (mAdapter.getItemCount() == 0) {
            listener.hideDataLayout();
        }
        if (sms == null)
            return;
        mAdapter.receiverSmsAdd(sms);
        rvContainer.scrollToPosition(0);
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

    @Override
    public RecyclerView getRvContainer() {
        return rvContainer;
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
        mPopupWindow.showAtLocation(rvContainer, Gravity.TOP, 0, SizeUtils.getSubTitleHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_close:
                mAdapter.closeWindowBtn();
                break;
            case R.id.iv_window_delete:
                if (SmsUtils.setDefaultSms(rvContainer, getActivity())) {
                    mAdapter.deleteWindowBtn();
                }
                break;
            case R.id.iv_window_add:
                Toast.makeText(getActivity(), JvApplication.getInstance().getString(R.string.function_not), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 搜索框监听事件等 ..
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mSmsBeans == null) return true;
        final List<SmsBean> filteredModelList = filter(mSmsBeans, newText);

        mAdapter.setFilter(filteredModelList);
        rvContainer.scrollToPosition(0);
        return false;
    }

    @Override
    public boolean onClose() {
        mAdapter.setFilter(mSmsBeans);
        return true;
    }

    /**
     * 过滤器
     *
     * @param smsBeans
     * @param query
     * @return
     */
    private List<SmsBean> filter(List<SmsBean> smsBeans, String query) {
        query = query.toLowerCase(); //小写

        final List<SmsBean> filteredModelList = new LinkedList<>();
        for (SmsBean smsBean : smsBeans) {

            final String name = smsBean.getName().toLowerCase();
            final String smsBody = smsBean.getSmsBody().toLowerCase();

            if (name.contains(query) || smsBody.contains(query)) {
                smsBean.selectStr = query; //添加选中字体记录
                filteredModelList.add(smsBean);
            }
        }
        return filteredModelList;
    }

}
