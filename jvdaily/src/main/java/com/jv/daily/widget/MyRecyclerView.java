package com.jv.daily.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyRecyclerView extends RecyclerView {
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton fab;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 自定义 添加布局管理器 方法
     *
     * @return
     */
    public MyRecyclerView addLinerLayoutManager(Context context) {
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setSmoothScrollbarEnabled(true);
        layout.setAutoMeasureEnabled(true);
        setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) layout;
        }
        return this;
    }

    /**
     * 自定义 添加布局管理器 方法
     *
     * @param animator
     * @return
     */
    public MyRecyclerView addItemAnimator(ItemAnimator animator) {
        setItemAnimator(animator);
        return this;
    }

    /**
     * 自定义 添加适配器 方法
     *
     * @param adapter
     * @return
     */
    public MyRecyclerView addAdapter(Adapter adapter) {
        setAdapter(adapter);
        return this;
    }

    /**
     * 自定义 添加布局size 方法
     *
     * @param hasFixedSize
     * @return
     */
    public MyRecyclerView addHasFixedSize(boolean hasFixedSize) {
        setHasFixedSize(hasFixedSize);
        return this;
    }

    public MyRecyclerView addNestedScrollingEnabled(boolean enabled) {
        setNestedScrollingEnabled(enabled);
        return this;
    }

    public MyRecyclerView addFab(FloatingActionButton fab) {
        this.fab = fab;
        return this;
    }

    /**
     * 加载更多接口 及 实现 监听逻辑 ---------------------------------------
     */
    @Override
    public void onScrollStateChanged(int state) {
        Log.i("WIDGET", linearLayoutManager.findFirstVisibleItemPosition() + "");
        if (state == RecyclerView.SCROLL_STATE_IDLE &&
                linearLayoutManager.findLastVisibleItemPosition() >= getAdapter().getItemCount() - 1) {
            Log.i("WIDGET", linearLayoutManager.findFirstVisibleItemPosition() + "");
            if (onLoadInterface != null) {
                onLoadInterface.onLoad();
            }
        }
//        if (state == RecyclerView.SCROLL_STATE_IDLE && linearLayoutManager.findFirstVisibleItemPosition() <= 3) {
//            Log.i("WIDGET", linearLayoutManager.findFirstVisibleItemPosition() + "");
//            fab.setVisibility(VISIBLE);
//        } else {
//            fab.setVisibility(GONE);
//        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.i("RV", "dxC : " + dxConsumed + "\n dyC : " + dyConsumed + "\n dxU :" + dxUnconsumed + " \n dyU :" + dyUnconsumed);

    }

    public interface OnLoadInterface {
        void onLoad();
    }

    public OnLoadInterface onLoadInterface;

    public MyRecyclerView addOnLoadInterface(OnLoadInterface onLoadInterface) {
        this.onLoadInterface = onLoadInterface;
        return this;
    }

}
