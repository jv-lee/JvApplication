package com.jv.daily.ui.main.adapter;

import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jv.daily.R;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class NewsAdapter extends BaseMultiItemQuickAdapter<StoriesBean, BaseViewHolder> {

    public NewsAdapter(List<StoriesBean> data) {
        super(data);
        addItemType(StoriesBean.CONTENT, R.layout.item_news);
        addItemType(StoriesBean.TITLE, R.layout.item_news_title);
    }

    @Override
    public List<StoriesBean> getData() {
        return super.getData();
    }

    public List<String> getIdList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            long id = getData().get(i).getId();
            if (id != 0) {
                list.add(String.valueOf(id));
            }
        }
        return list;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoriesBean item) {
        switch (helper.getItemViewType()) {
            case StoriesBean.TITLE:
                helper.setText(R.id.tv_news_title, item.getDate());
                break;
            case StoriesBean.CONTENT:
                helper.setText(R.id.tv_news_text, item.getTitle())
                        .addOnClickListener(R.id.iv_news_pic);
                TextView textView = helper.getView(R.id.tv_news_text);

                if (item.isMultipic()) {
                    textView.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                } else {
                    textView.setTextColor(mContext.getResources().getColor(R.color.colorTextDark));
                }
                GlideUtil.glideUrl(mContext, item.getImages().get(0), helper.getView(R.id.iv_news_pic));
                break;
        }
    }
}
