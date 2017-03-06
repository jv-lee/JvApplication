package com.jv.daily.adapter.item;


import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jv.daily.R;
import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.app.MyApplication;
import com.jv.daily.mvp.module.NewsBean;
import com.jv.daily.widget.BannerView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class HeaderItem extends BaseItem {
    @Override
    public int getLayout() {
        return R.layout.item_header;
    }

    private final List<NewsBean.TopStoriesBean> beans;

    public HeaderItem(MultiTypeAdapter adapter, final List<NewsBean.TopStoriesBean> beans) {
        this.beans = beans;

        setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MyApplication.getInstance(), "" + beans.get(position).getTitle() +" index "+position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getJson() {
        return new Gson().toJson(beans);
    }


}
