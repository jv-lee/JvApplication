package com.jv.daily.adapter.item;

import android.view.View;


import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.BR;
import com.jv.daily.widget.BannerView;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class BaseItem implements MultiTypeAdapter.ItemInterface {
    @Override
    public int getVariableId() {
        return BR.item;
    }

    //所有监听 提供实例 和 get set 方法
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private BannerView.OnBannerItemClickListener onBannerItemClickListener;

    public View.OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public BannerView.OnBannerItemClickListener getOnBannerItemClickListener() {
        return onBannerItemClickListener;
    }

    public BaseItem setOnBannerItemClickListener(BannerView.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
        return this;
    }

    public BaseItem setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public BaseItem setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        return this;
    }

}
