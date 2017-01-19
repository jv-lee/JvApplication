package com.jv.sms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.app.JvApplication;
import com.jv.sms.utils.BarUtils;
import com.jv.sms.utils.SmsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断当前应用是否为系统默认应用
        if (SmsUtils.hasDefaultsSmsApplication(JvApplication.getInstance())) {
            startSmsActivity();
        }

        //set视图 注入View
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        //获取全屏试图容器
        mDecorView = getWindow().getDecorView();

        BarUtils.hideBar(mDecorView); //隐藏状态栏 和控制栏

        int navigationHeight = BarUtils.getNavigationBarHeight(this);
        if (navigationHeight != 0)

        {
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
//        String packageName = getPackageName();
//        if (!Telephony.Sms.getDefaultSmsPackage(WelcomeActivity.this).equals(packageName)) {
//            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
//            startActivityForResult(intent, REQUEST_CODE);
//        }
        SmsUtils.hasDefaultSmsApplicationStartSettings(this, REQUEST_CODE);
    }

    /**
     * 直接进入主页
     *
     * @param view
     */
    @OnClick(R.id.tv_out)
    public void outClick(View view) {
        startSmsActivity();
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
            startSmsActivity();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 跳转到主页函数
     */

    public void startSmsActivity() {
        startActivity(new Intent(this, SmsActivity.class));
        finish();
    }
}
