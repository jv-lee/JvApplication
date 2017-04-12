package com.jv.daily.ui.main.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jv.daily.R;
import com.jv.daily.bean.StoriesBean;
import com.jv.daily.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class NewsAdapter extends BaseQuickAdapter<StoriesBean, BaseViewHolder> {

    public NewsAdapter(List<StoriesBean> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoriesBean item) {
        helper.setText(R.id.tv_news_text, item.getTitle());
        if (item.isMultipic()) {
            helper.setTextColor(R.id.tv_news_text, R.color.colorTextDark);
        } else {
            helper.setTextColor(R.id.tv_news_text, R.color.colorTheme);
        }
        GlideUtil.glideUrl(mContext,item.getImages().get(0),helper.getView(R.id.iv_news_pic));
    }
}
