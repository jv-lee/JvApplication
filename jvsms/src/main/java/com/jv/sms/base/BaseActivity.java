package com.jv.sms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

import com.jv.sms.widget.swipebacklayout.SwipeBackActivity;
import com.jv.sms.widget.swipebacklayout.SwipeBackLayout;

/**
 * Created by Administrator on 2016/12/14.
 */

public abstract class BaseActivity extends SwipeBackActivity {

    public abstract int getContentViewId();

    public SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);

        mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(350);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        initAllView(savedInstanceState);
    }

    protected abstract void setThemes();

    protected abstract void initAllView(Bundle savedInstanceState);

}
