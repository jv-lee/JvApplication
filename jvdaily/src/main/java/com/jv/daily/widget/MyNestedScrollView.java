package com.jv.daily.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/3/1.
 */

public class MyNestedScrollView extends NestedScrollView {
    FloatingActionButton fab;

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (fab != null) {
            if (t >= 2000 && fab != null) {
                fab.setVisibility(VISIBLE);
            } else {
                fab.setVisibility(GONE);
            }
        }
    }
}
