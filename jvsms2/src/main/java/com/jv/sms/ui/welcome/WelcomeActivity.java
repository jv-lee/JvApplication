package com.jv.sms.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.ui.MainActivity;
import com.jv.sms.ui.sms.SmsActivity;
import com.jv.sms.utils.BarUtils;
import com.jv.sms.utils.IntentUtil;
import com.jv.sms.utils.SmsUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    private View mDecorView;
    @BindView(R.id.tv_out)
    TextView tvOut;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_navigation_layout)
    LinearLayout llNavigationLayout;

    //请求返回码
    private final int REQUEST_CODE = 0x02222;

    @Override
    protected int bindRootView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void bindData() {
        //        //加载热更新补丁
//        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
//                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");

        //判断当前应用是否为系统默认应用
        if (SmsUtils.hasDefaultsSmsApplication(mContext)) {
            IntentUtil.startActivityOrFinish(this, MainActivity.class);
        }

        mDecorView = getWindow().getDecorView();//获取全屏试图容器
        BarUtils.hideBar(mDecorView); //隐藏状态栏 和控制栏

        //判断当前没有控制栏 适配window高度
        int navigationHeight = BarUtils.getNavigationBarHeight(this);
        if (navigationHeight != 0) {
            llNavigationLayout.setWeightSum(0);
            llNavigationLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navigationHeight));
        }
    }

    //获取焦点时隐藏
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            BarUtils.hideBar(mDecorView);
        }
    }

    /**
     * 点击Next 发起设置当前应用为默认短信应用方法
     *
     * @param view
     */
    @OnClick(R.id.tv_next)
    public void nextClick(View view) {
        SmsUtils.hasDefaultSmsApplicationStartSettings(this, REQUEST_CODE);
    }

    /**
     * 直接进入主页
     *
     * @param view
     */
    @OnClick(R.id.tv_out)
    public void outClick(View view) {
        IntentUtil.startActivityOrFinish(this, SmsActivity.class);
    }

    /**
     * 根据返回值判断是否进入主页
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            IntentUtil.startActivityOrFinish(this, SmsActivity.class);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
