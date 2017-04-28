package com.jv.sms.ui.content;

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
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.base.mvp.BaseFragment;
import com.jv.sms.constant.Constant;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.interfaces.ToolbarSetListener;
import com.jv.sms.rx.EventBase;
import com.jv.sms.rx.RxBus;
import com.jv.sms.ui.content.adapter.ContentAdapter;
import com.jv.sms.ui.content.adapter.ForwardDialogAdapter;
import com.jv.sms.ui.newsms.NewSmsActivity;
import com.jv.sms.utils.ClickUtils;
import com.jv.sms.utils.ShareUtils;
import com.jv.sms.utils.SizeUtils;
import com.jv.sms.utils.SmsUtils;
import com.jv.sms.utils.SystemUtils;
import com.jv.sms.utils.TelUtils;
import com.jv.sms.utils.TimeUtils;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconsFragment;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/4/28.
 */
@SuppressLint("ValidFragment")
public class ContentFragment extends BaseFragment<ContentContract.Presenter> implements ContentContract.View, View.OnClickListener,
        ContentAdapter.OnSmsListAdapterListener {


    @BindView(R.id.et_smsContent)
    EmojiconEditText etSmsContent;
    @BindView(R.id.ll_content_layout)
    LinearLayout llContentLayout;
    @BindView(R.id.rv_container)
    RecyclerView rvContainer;
    @BindView(R.id.iv_addSms)
    ImageView ivAddSms;
    @BindView(R.id.cb_emojIcon)
    AppCompatCheckBox cbEmojiIcon;
    @BindView(R.id.iv_sendSms)
    ImageView ivSendSms;
    @BindView(R.id.fl_emojFragment_container)
    FrameLayout flEjmoContainer;

    @Inject
    RxBus rxBus;

    //弹窗View
    private PopupWindow mPopupWindow;
    private View popupView;

    //Dialog
    AlertDialog deleteDialog;
    AlertDialog forwardDialog;

    //toolbar监听回调接口
    private ToolbarSetListener toolbarSetListener;


    //数据 and 适配器
    private LinkedList<SmsBean> mSmsBeans;
    private ContentAdapter mAdapter;

    //中转数据实体类
    private SmsBean bean;

    //输入框flag
    private boolean etFlag = true;

    //发送短信成功返回码
    private final String SENT_SMS_ACTION = "send_sms_action_code";

    //表情窗体参数
    private int emotionHeight;
    private EmojiconsFragment emojiconsFragment;


    public ContentFragment(ToolbarSetListener toolbarSetListener) {
        this.toolbarSetListener = toolbarSetListener;
    }

    @Override
    protected void onCreate() {
        //获取当前跳转实体数据 号码 、名字 、会话Id 、颜色
        bean = (SmsBean) getActivity().getIntent().getSerializableExtra("bean");

        //当前显示的会话Fragment 是哪一个 记录FLAG
        Constant.THIS_SMS_FRAGMENT_FLAG = bean.getPhoneNumber();

        //注册发送短信通知广播
        getActivity().registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
    }

    @Override
    protected View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, null, false);
    }

    @Override
    protected void bindData() {
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return rxBus.register(this);
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

    @Override
    public void onResume() {
        super.onResume();
        toolbarSetListener.setToolbarTitle(bean.getName());
        if (Constant.text != null && !Constant.text.equals("")) {
            etSmsContent.setText(Constant.text);
            Constant.text = "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        TimeUtils.clearTimeList();//清空列表时间差管理集合
        getActivity().unregisterReceiver(sendMessage);//注销广播
        Constant.THIS_SMS_FRAGMENT_FLAG = "";//将当前全局号码初始化

        //退出时刷新显示
        if (mAdapter == null) return;
        if (mAdapter.getItemCount() == 0) {
            rxBus.post(new EventBase(Constant.RX_CODE_DELETE_THREAD_ID, bean.getThread_id()));
        } else {
            rxBus.post(new EventBase(Constant.RX_CODE_UPDATE_MESSAGE, mSmsBeans.get(0)));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call: // 菜单点击拨打当前联系人电话
                TelUtils.sendTel1(mActivity, bean.getPhoneNumber());
                break;
            case R.id.menu_item_delete: // 菜单点击删除当前联系人会话
                if (SmsUtils.setDefaultSms(rvContainer, mActivity)) {
                    if (deleteDialog == null) {
                        deleteDialog = new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.prompt))
                                .setMessage(mActivity.getString(R.string.has_delete_session))
                                .setNegativeButton(mActivity.getString(R.string.str_dialog_no), null)
                                .setPositiveButton(mActivity.getString(R.string.str_dialog_yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPresenter.deleteSmsByThreadId(bean.getThread_id());
                                    }
                                }).create();
                    }
                    deleteDialog.show();

                }
                break;
            case R.id.menu_item_addContacts: // 菜单点击 添加当前会话人 为联系人
                Toast.makeText(mActivity, "添加联系人功能正在开发", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) { //按下BACK 键先清除当前选中状态
            if (flEjmoContainer.isShown()) {
                hideEmotionView(true);
                return true;
            }
            return mAdapter.clearSelectMessageState();
        }
        return false;
    }

    /**
     * 填充数据回调接口
     *
     * @param list
     */
    @Override
    public void setSmsBeansAll(LinkedList<SmsBean> list) {
        if (mSmsBeans == null) {
            mSmsBeans = list;
            mAdapter = new ContentAdapter(getActivity(), mSmsBeans, this);
            rvContainer.setAdapter(mAdapter);
        } else {
            mSmsBeans.clear();
            mSmsBeans = list;
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyItemRangeInserted(0, list.size());
    }

    @Override
    public void showSmsListSuccess() {
        toolbarSetListener.hideProgressBar();
    }

    @Override
    public void showSmsError() {
        toolbarSetListener.hideProgressBar();
    }

    @Override
    public void deleteSmsSuccess() {
        mAdapter.windowDeleteResult();
    }

    @Override
    public void deleteSmsError() {
        Toast.makeText(getActivity(), "delete sms error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendSmsLoading(SmsBean smsBean) {
        //设置发送短信内容至显示
        rxBus.post(new EventBase(smsBean.getPhoneNumber(), smsBean));
        etSmsContent.setText("");
        Constant.smsBean = smsBean; //当前浏览短信实体
    }

    @Override
    public void sendSmsSuccess(SmsBean smsBean) {
        mAdapter.initHasSendProgressbar();
    }

    @Override
    public void sendSmsError(SmsBean smsBean) {
        mAdapter.sendSmsError(smsBean);
    }

    @Override
    public void deleteThreadSuccess() {
        getActivity().finish(); //删除当前会话成功 后关闭当前会话
        rxBus.post(new EventBase(Constant.RX_CODE_DELETE_THREAD_ID, bean.getThread_id())); //发送通知删除会话列表当前会话
    }

    @Override
    public void deleteThreadError() {
        Toast.makeText(mActivity, mActivity.getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reSendSms(boolean flag, int position) {
        if (flag) {
            mAdapter.statusIconClickResult(position);
        } else {
            Toast.makeText(mActivity, "reSend sms error", Toast.LENGTH_SHORT).show();
        }
    }


    //发送短信状态回调广播
    BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == Activity.RESULT_OK) {
                mPresenter.sendSmsSuccess(Constant.smsBean);
            } else {
                mPresenter.sendSmsError(Constant.smsBean);
            }
        }
    };

    /**
     * 发送短信
     */
    public void sendSms(String content, String phoneNumber) {
        if (content.length() > 0) {
            Intent sendIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sendPi = PendingIntent.getBroadcast(getActivity(), 0, sendIntent, 0);
            mPresenter.sendSms(sendPi, phoneNumber, content, System.currentTimeMillis());
            ClickUtils.sendMusic();
        }
    }

    @Override
    public RecyclerView getRvContainer() {
        return rvContainer;
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
    public ContentContract.Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void getSendSms(String content, String phoneNumber) {
        sendSms(content, phoneNumber);
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
        mPopupWindow.showAtLocation(rvContainer, Gravity.TOP, 0, SizeUtils.getSubTitleHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_window_close:
                mAdapter.clearSelectMessageState();
                break;
            case R.id.iv_window_attachment:
                ShareUtils.shareText(mActivity, mSmsBeans.get(mAdapter.smsListUiFlagBean.getSelectMessageUiPosition()).getSmsBody());
                mAdapter.clearSelectMessageState();
                break;
            case R.id.iv_window_forward:
                showForward(mSmsBeans.get(mAdapter.smsListUiFlagBean.getSelectMessageUiPosition()).getSmsBody());
                mAdapter.clearSelectMessageState();
                break;
            case R.id.iv_window_copy:
                mAdapter.windowCopy();
                break;
            case R.id.iv_window_info:
                mAdapter.windowInfo();
                break;
            case R.id.iv_window_delete:
                if (SmsUtils.setDefaultSms(rvContainer, mActivity)) {
                    mAdapter.windowDelete();
                }
                break;
        }
    }

    /**
     * 转发短信弹窗
     *
     * @param text
     */
    private void showForward(final String text) {

        if (forwardDialog == null) {
            //初始化转发列表View
            RecyclerView rvForwardView = new RecyclerView(mActivity);
            rvForwardView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rvForwardView.setLayoutManager(new LinearLayoutManager(mActivity));
            rvForwardView.setAdapter(new ForwardDialogAdapter(mActivity, text));

            forwardDialog = new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.forward_message))
                    .setView(rvForwardView)
                    .setNegativeButton(mActivity.getString(R.string.str_dialog_no), null)
                    .setPositiveButton(mActivity.getString(R.string.new_sms), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Constant.text = text;
                            startActivity(new Intent(mActivity, NewSmsActivity.class));
                        }
                    }).create();
        }

        forwardDialog.show();

    }

    private void createEjmoLayout(Bundle savedInstanceState) {

        /**安全判断 有些情况会出现异常**/
        if (savedInstanceState == null) {
            emojiconsFragment = EmojiconsFragment.newInstance(false);
            getFragmentManager().beginTransaction().add(R.id.fl_emojFragment_container, emojiconsFragment, "EmotionFragemnt").commit();
        } else {
            emojiconsFragment = (EmojiconsFragment) getFragmentManager().findFragmentByTag("EmotionFragemnt");
        }
    }


    /**
     * 隐藏emoji
     **/
    private void hideEmotionView(boolean showKeyBoard) {
        cbEmojiIcon.setChecked(false);
        if (flEjmoContainer.isShown()) {
            if (showKeyBoard) {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) llContentLayout.getLayoutParams();
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
        cbEmojiIcon.setChecked(true);
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
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) llContentLayout.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) llContentLayout.getLayoutParams()).weight = 1.0F;
    }
}
