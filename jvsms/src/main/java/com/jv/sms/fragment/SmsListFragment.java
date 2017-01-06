package com.jv.sms.fragment;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.NewSmsActivity;
import com.jv.sms.adapter.ForwardDialogAdapter;
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
import com.jv.sms.utils.ShareUtils;
import com.jv.sms.utils.SizeUtils;
import com.jv.sms.utils.SystemUtils;
import com.jv.sms.utils.TelUtils;
import com.jv.sms.utils.TimeUtils;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmsListFragment extends BaseFragment implements ISmsListView, View.OnClickListener,
        SmsListDataAdapter.OnSmsListAdapterListener {

    @BindView(R.id.ll_sms_list_layout)
    LinearLayout llSmsListLayout;
    @BindView(R.id.rv_smsListFragment_container)
    RecyclerView rvSmsListFragmentContainer;
    @BindView(R.id.iv_add_sms)
    ImageView ivAddSms;
    @BindView(R.id.et_sms_content)
    public EmojiconEditText etSmsContent;
    @BindView(R.id.cb_emoji_icon)
    AppCompatCheckBox cbEmojiIcon;
    @BindView(R.id.iv_send_sms)
    ImageView ivSendSms;
    @BindView(R.id.fl_ejmo_container)
    FrameLayout flEjmoContainer;

    //弹窗View
    private PopupWindow mPopupWindow;
    private View popupView;

    //toolbar监听回调接口
    private ToolbarSetListener toolbarSetListener;

    //当前控制层实现类
    private ISmsListPresenter mPresenter;

    //数据 and 适配器
    private LinkedList<SmsBean> mList;
    private SmsListDataAdapter mAdapter;

    //中转数据实体类
    private SmsBean bean;

    //输入框flag
    private boolean etFlag = true;

    //发送短信成功返回码
    private final String SENT_SMS_ACTION = "send_sms_action_code";

    //表情窗体参数
    private int emotionHeight;
    private EmojiconsFragment emojiconsFragment;

    public SmsListFragment() {
    }

    public SmsListFragment(ToolbarSetListener toolbarSetListener) {
        this.toolbarSetListener = toolbarSetListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取当前跳转实体数据 号码 、名字 、会话Id 、颜色
        bean = (SmsBean) getActivity().getIntent().getSerializableExtra("bean");

        //当前显示的会话Fragment 是哪一个 记录FLAG
        JvApplication.THIS_SMS_FRAGMENT_FLAG = bean.getPhoneNumber();

        //实例化Presenter  and 注册发送短信通知广播
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
        initPopupView(); //初始化长按弹窗
        ivAddSms.setColorFilter(ContextCompat.getColor(mContext, JvApplication.icon_theme_darkColors[bean.getColorPosition()])); //设置添加按钮颜色

        //初始化消息显示列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);//设置倒叙显示消息列表
        rvSmsListFragmentContainer.setLayoutManager(linearLayoutManager);
        rvSmsListFragmentContainer.setItemAnimator(new DefaultItemAnimator());
        rvSmsListFragmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了 rv", Toast.LENGTH_SHORT).show();
            }
        });

        //加载数据
        mPresenter.refreshSmsList(bean.getThread_id());

        //初始化Ejmo表情
        createEjmoLayout(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call: // 菜单点击拨打当前联系人电话
                TelUtils.sendTel1(mContext, bean.getPhoneNumber());
                break;
            case R.id.menu_item_delete: // 菜单点击删除当前联系人会话
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
            case R.id.menu_item_addContacts: // 菜单点击 添加当前会话人 为联系人
                Toast.makeText(mContext, "添加联系人功能正在开发", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) { //按下BACK 键先清除当前选中状态
            if (flEjmoContainer.isShown()) {
                hideEmotionView(true);
                cbEmojiIcon.setChecked(false);
                return true;
            }
            return mAdapter.clearSelectMessageState();
        }
        return true;
    }

    @OnClick({R.id.iv_add_sms, R.id.iv_send_sms})
    public void ivOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_send_sms: //发送短信
                sendSms();
                break;
            case R.id.iv_add_sms:
                Toast.makeText(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.et_sms_content:
                hideEmotionView(true);
                break;
        }
    }

    @OnCheckedChanged(R.id.cb_emoji_icon)
    public void onChecked(CompoundButton button, boolean isChecked) {
        if (isChecked && !flEjmoContainer.isShown()) {
            showEmotionView();
        } else {
            hideEmotionView(true);
        }
    }

    /**
     * 动态改变发送按钮颜色
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
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
                    public void call(EventBase eventBase) { //收到新增短信通知 判断会话号码 做逻辑操作
                        if (eventBase.getOption().equals(bean.getPhoneNumber())) {
                            mAdapter.insertSmsListUi((SmsBean) eventBase.getObj());
                        }
                    }
                });
    }

    /**
     * 填充数据回调接口
     *
     * @param list
     */
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
        mAdapter.notifyItemRangeInserted(0, list.size());
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
        if (JvApplication.text != null && !JvApplication.text.equals("")) {
            etSmsContent.setText(JvApplication.text);
            JvApplication.text = "";
        }
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

    /**
     * 发送短信
     */
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
                ShareUtils.shareText(mContext, mList.get(mAdapter.smsListUiFlagBean.getSelectMessageUiPosition()).getSmsBody());
                mAdapter.clearSelectMessageState();
                break;
            case R.id.iv_window_forward:
                showForward(mList.get(mAdapter.smsListUiFlagBean.getSelectMessageUiPosition()).getSmsBody());
                mAdapter.clearSelectMessageState();
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

    /**
     * 转发短信弹窗
     *
     * @param text
     */
    private void showForward(final String text) {

        //初始化转发列表View
        RecyclerView rvForwardView = new RecyclerView(mContext);
        rvForwardView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rvForwardView.setLayoutManager(new LinearLayoutManager(mContext));
        rvForwardView.setAdapter(new ForwardDialogAdapter(mContext, text));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("转发信息")
                .setView(rvForwardView)
                .setNegativeButton("取消", null)
                .setPositiveButton("新信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JvApplication.text = text;
                        startActivity(new Intent(mContext, NewSmsActivity.class));
                    }
                }).create().show();
    }

    private void createEjmoLayout(Bundle savedInstanceState) {

        /**安全判断 有些情况会出现异常**/
        if (savedInstanceState == null) {
            emojiconsFragment = EmojiconsFragment.newInstance(false);
            getFragmentManager().beginTransaction().add(R.id.fl_ejmo_container, emojiconsFragment, "EmotionFragemnt").commit();
        } else {
            emojiconsFragment = (EmojiconsFragment) getFragmentManager().findFragmentByTag("EmotionFragemnt");
        }
    }


    /**
     * 隐藏emoji
     **/
    private void hideEmotionView(boolean showKeyBoard) {
        if (flEjmoContainer.isShown()) {
            if (showKeyBoard) {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) llSmsListLayout.getLayoutParams();
                localLayoutParams.height = flEjmoContainer.getTop();
                localLayoutParams.weight = 0.0F;
                flEjmoContainer.setVisibility(View.GONE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SystemUtils.showKeyBoard(etSmsContent);
                etSmsContent.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }

                }, 200L);
            } else {
                flEjmoContainer.setVisibility(View.GONE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                unlockContainerHeightDelayed();
            }
        }
    }

    private void showEmotionView() {

        emotionHeight = SystemUtils.getKeyboardHeight(getActivity());

        SystemUtils.hideSoftInput(etSmsContent);
        flEjmoContainer.getLayoutParams().height = emotionHeight;
        flEjmoContainer.setVisibility(View.VISIBLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //在5.0有navigationbar的手机，高度高了一个statusBar
        int lockHeight = SystemUtils.getAppContentHeight(getActivity());
        lockContainerHeight(lockHeight);
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) llSmsListLayout.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) llSmsListLayout.getLayoutParams()).weight = 1.0F;
    }

}