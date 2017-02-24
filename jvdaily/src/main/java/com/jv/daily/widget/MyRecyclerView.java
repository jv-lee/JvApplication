package com.jv.daily.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyRecyclerView extends RecyclerView {
    private LinearLayoutManager linearLayoutManager;

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
     * @param layout
     * @return
     */
    public MyRecyclerView addLayoutManager(LayoutManager layout) {
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

    /**
     * 加载更多接口 及 实现 监听逻辑 ---------------------------------------
     */
    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE &&
                linearLayoutManager.findLastVisibleItemPosition() >= getAdapter().getItemCount() - 1) {
            if (onLoadInterface != null) {
                onLoadInterface.onLoad();
            }
        }
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
